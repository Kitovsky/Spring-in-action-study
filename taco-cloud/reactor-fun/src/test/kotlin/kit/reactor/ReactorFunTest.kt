package kit.reactor

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import java.time.Duration
import java.util.function.BiFunction
import java.util.stream.Stream

internal class ReactorFunTest {

    @Test
    fun createFluxJust() {
        val fruitFlux = Flux.just("Apple", "Orange", "Grape", "Banana", "Strawberry")
        fruitFlux.subscribe {
            println("Here's fruits: $it")
        }
    }

    @Test
    internal fun verifyFluxTest() {
        verifyFlux(Flux.just("Apple", "Orange", "Grape", "Banana", "Strawberry"))
    }

    private fun verifyFlux(flux: Flux<String>) {
        StepVerifier.create(flux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete()
    }

    @Test
    internal fun createFluxArray() {
        verifyFlux(Flux.fromArray(arrayOf("Apple", "Orange", "Grape", "Banana", "Strawberry")))
    }

    @Test
    internal fun createFluxList() {
        verifyFlux(Flux.fromIterable(listOf("Apple", "Orange", "Grape", "Banana", "Strawberry")))
    }

    @Test
    internal fun createFluxStream() {
        verifyFlux(Flux.fromStream(Stream.of("Apple", "Orange", "Grape", "Banana", "Strawberry")))
    }

    @Test
    internal fun createFluxRange() {
        val rangeFlux = Flux.range(1, 5)
        StepVerifier.create(rangeFlux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete()
    }

    @Test
    internal fun createFluxInterval() {
        val intervalFlux = Flux.interval(Duration.ofSeconds(1))
                .take(5)
        StepVerifier.create(intervalFlux)
                .expectNext(0)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .verifyComplete()
    }

    @Test
    internal fun mergeFluxes() {
        val characterFlux = Flux.just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500))
        val foodFlux = Flux.just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500))
        val mergeFlux = characterFlux.mergeWith(foodFlux)
        StepVerifier.create(mergeFlux)
                .expectNext("Garfield")
                .expectNext("Lasagna")
                .expectNext("Kojak")
                .expectNext("Lollipops")
                .expectNext("Barbossa")
                .expectNext("Apples")
                .verifyComplete()
    }

    @Test
    internal fun zipFlux() {
        val characterFlux = Flux.just("Garfield", "Kojak", "Barbossa")
        val foodFlux = Flux.just("Lasagna", "Lollipops", "Apples")
        val zipFlux = Flux.zip(characterFlux, foodFlux)
        StepVerifier.create(zipFlux)
                .expectNextMatches { it.t1 == "Garfield" && it.t2 == "Lasagna" }
                .expectNextMatches { it.t1 == "Kojak" && it.t2 == "Lollipops" }
                .expectNextMatches { it.t1 == "Barbossa" && it.t2 == "Apples" }
                .verifyComplete()

    }

    @Test
    internal fun zipFluxes2Object() {
        val characterFlux = Flux.just("Garfield", "Kojak", "Barbossa")
        val foodFlux = Flux.just("Lasagna", "Lollipops", "Apples")
        val zipFlux = Flux.zip(characterFlux, foodFlux, BiFunction { c: String, f: String -> "$c eats $f" })
        StepVerifier.create(zipFlux)
                .expectNext("Garfield eats Lasagna")
                .expectNext("Kojak eats Lollipops")
                .expectNext("Barbossa eats Apples")
                .verifyComplete()

    }

    @Test
    internal fun firstFlux() {
        val slowFlux = Flux.just("tortoise", "snail", "sloth")
                .delaySubscription(Duration.ofMillis(100))
        val fastFlux = Flux.just("hare", "cheetah", "squirrel")
        val firstFlux = Flux.first(slowFlux, fastFlux)
        StepVerifier.create(firstFlux)
                .expectNext("hare")
                .expectNext("cheetah")
                .expectNext("squirrel")
                .verifyComplete()
    }

    @Test
    internal fun skipFewFlux() {
        val skipFlux = Flux.just("one", "two", "skip", "ninety nine", "one hundred")
                .skip(3)
        StepVerifier.create(skipFlux)
                .expectNext("ninety nine")
                .expectNext("one hundred")
                .verifyComplete()
    }

    @Test
    internal fun skipSecondsFlux() {
        val skipFlux = Flux.just("one", "two", "skip", "ninety nine", "one hundred")
                .delayElements(Duration.ofSeconds(1))
                .skip(Duration.ofSeconds(4))
        StepVerifier.create(skipFlux)
                .expectNext("ninety nine")
                .expectNext("one hundred")
                .verifyComplete()

    }

    @Test
    internal fun takeFewFlux() {
        val takeFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .take(3)
        StepVerifier.create(takeFlux)
                .expectNext("Yellowstone", "Yosemite", "Grand Canyon")
                .verifyComplete()

    }

    @Test
    internal fun takeSecondsFlux() {
        val takeFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .delayElements(Duration.ofSeconds(1))
                .take(Duration.ofMillis(3500))
        StepVerifier.create(takeFlux)
                .expectNext("Yellowstone", "Yosemite", "Grand Canyon")
                .verifyComplete()
    }

    @Test
    internal fun noSpaceFilter() {
        val filerFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
                .filter { !it.contains(" ") }
        StepVerifier.create(filerFlux)
                .expectNext("Yellowstone", "Yosemite", "Zion")
                .verifyComplete()
    }

    @Test
    internal fun distinctFlux() {
        val distinctFlux = Flux.just("dog", "bird", "cat", "dog", "snake", "bird")
                .distinct()
        StepVerifier.create(distinctFlux)
                .expectNext("dog", "bird", "cat", "snake")
                .verifyComplete()
    }

    @Test
    internal fun mapFlux() {
        val mapFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .map { Player(it.substringBefore(" "), it.substringAfter(" ")) }
        StepVerifier.create(mapFlux)
                .expectNext(Player("Michael", "Jordan"))
                .expectNext(Player("Scottie", "Pippen"))
                .expectNext(Player("Steve", "Kerr"))
                .verifyComplete()
    }

    @Test
    internal fun flatMapFlux() {
        val flatMapFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .flatMap { s ->
                    Mono.just(s)
                            .map { Player(it.substringBefore(" "), it.substringAfter(" ")) }
                            .subscribeOn(Schedulers.parallel())
                }
        val players = listOf(Player("Michael", "Jordan"),
                Player("Scottie", "Pippen"),
                Player("Steve", "Kerr"))
        StepVerifier.create(flatMapFlux)
                .expectNextMatches { players.contains(it) }
                .expectNextMatches { players.contains(it) }
                .expectNextMatches { players.contains(it) }
                .verifyComplete()

    }

    private data class Player(val name: String, val surename: String)

    @Test
    internal fun bufferFlux() {
        val flux = Flux.just("orange", "apple", "banana", "kiwi", "strawberry")
        val bufferFlux = flux.buffer(3)
        StepVerifier.create(bufferFlux)
                .expectNext(listOf("orange", "apple", "banana"))
                .expectNext(listOf("kiwi", "strawberry"))
                .verifyComplete()
    }

    @Test
    internal fun flatMapBufferFlux() {
        val flux = Flux.just("orange", "apple", "banana", "kiwi", "strawberry")
                .buffer(3)
                .flatMap {
                    Flux.fromIterable(it)
                            .map(String::toUpperCase)
                            .subscribeOn(Schedulers.parallel())
                            .log()
                }
        val fruits = listOf("orange", "apple", "banana", "kiwi", "strawberry").map(String::toUpperCase)
        StepVerifier.create(flux)
                .expectNextMatches(fruits::contains)
                .expectNextMatches(fruits::contains)
                .expectNextMatches(fruits::contains)
                .expectNextMatches(fruits::contains)
                .expectNextMatches(fruits::contains)
                .verifyComplete()
    }

    @Test
    internal fun collectFlux() {
        val fruitFlux = Flux.just("orange", "apple", "banana", "kiwi", "strawberry")
        val flux: Flux<List<String>> = fruitFlux.buffer()
        StepVerifier.create(flux)
                .expectNext(listOf("orange", "apple", "banana", "kiwi", "strawberry"))
                .verifyComplete()

        val mono: Mono<List<String>> = fruitFlux.collectList()
        StepVerifier.create(mono)
                .expectNext(listOf("orange", "apple", "banana", "kiwi", "strawberry"))
                .verifyComplete()
    }

    @Test
    internal fun collectMapFlux() {
        val animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo")
        val mono: Mono<Map<Char, String>> = animalFlux.collectMap { it[0] }
        StepVerifier.create(mono)
                .expectNextMatches {
                    it.size == 3
                            && it['a'] == "aardvark"
                            && it['e'] == "eagle"
                            && it['k'] == "kangaroo"
                }
                .verifyComplete()
    }

    @Test
    internal fun allFlux() {
        val animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo")
        val trueMono = animalFlux.all { it.contains('a') }
        StepVerifier.create(trueMono)
                .expectNext(true)
                .verifyComplete()

        val falseMono = animalFlux.all{ it.contains('k')}
        StepVerifier.create(falseMono)
                .expectNext(false)
                .verifyComplete()
    }

    @Test
    internal fun anyFlux() {
        val animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo")
        val trueMono = animalFlux.any { it.contains('t') }
        StepVerifier.create(trueMono)
                .expectNext(true)
                .verifyComplete()

        val falseMono = animalFlux.any{ it.contains('z')}
        StepVerifier.create(falseMono)
                .expectNext(false)
                .verifyComplete()
    }
}