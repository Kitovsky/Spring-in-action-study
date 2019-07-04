package kit.reactor

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration
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
}