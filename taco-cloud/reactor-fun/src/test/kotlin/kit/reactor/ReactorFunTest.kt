package kit.reactor

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
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
}
