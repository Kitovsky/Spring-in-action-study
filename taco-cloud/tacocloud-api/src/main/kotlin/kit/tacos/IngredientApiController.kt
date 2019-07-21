package kit.tacos

import kit.tacos.data.IngredientRepository
import kit.tacos.domain.Ingredient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/ingredients", produces = [MediaType.APPLICATION_JSON_VALUE])
@CrossOrigin(origins = ["*"])
class IngredientApiController(
        @Autowired private val ingredientRepo: IngredientRepository
) {

//    @GetMapping
//    fun allIngredients(): Iterable<IngredientResource> {
//        return IngredientResource.ingredientAssembler.toResources(ingredientRepo.findAll())
//    }

    @GetMapping
    fun allIngredients(): Flux<Ingredient> = Flux.fromIterable(ingredientRepo.findAll())

    @GetMapping("/{id}")
    fun ingredientById(@PathVariable("id") id: String): Mono<ResponseEntity<Ingredient?>> =
            Mono.justOrEmpty(ingredientRepo.findById(id))
                    .map { ResponseEntity.ok(it) }
                    .defaultIfEmpty(ResponseEntity.notFound().build())

}