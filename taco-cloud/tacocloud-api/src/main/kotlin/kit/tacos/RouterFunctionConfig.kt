package kit.tacos

import kit.tacos.data.TacoRepository
import kit.tacos.domain.Taco
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.just
import java.net.URI

@Configuration
class RouterFunctionConfig(
        @Autowired private val tacoRepo: TacoRepository
) {
    @Bean
    fun routerFunction(): RouterFunction<*> = router {
        GET("/bye") { ok().body(just("See ya!"), String::class.java) }
        POST("/fun/design") { postTaco(it) }
    }

    private fun postTaco(request: ServerRequest): Mono<ServerResponse> {
        val taco = request.bodyToMono(Taco::class.java)
        return taco.map { tacoRepo.save(it) }
                .flatMap {
                    ServerResponse.created(URI.create("http://localhost:8085/design/taco/${it.id}"))
                            .body(taco, Taco::class.java)
                }
    }
}