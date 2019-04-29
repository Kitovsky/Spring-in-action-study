package kit.tacos

import kit.tacos.data.IngredientRepository
import kit.tacos.domain.Ingredient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.mvc.ResourceAssemblerSupport
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ingredients", produces = [MediaType.APPLICATION_JSON_VALUE])
@CrossOrigin(origins = ["*"])
class IngredientApiController(
        @Autowired private val ingredientRepo: IngredientRepository
) {

    @GetMapping
    fun allIngredients(): Iterable<IngredientResource> {
        return IngredientResource.ingredientAssembler.toResources(ingredientRepo.findAll())
    }

    @GetMapping("/{id}")
    fun ingredientById(@PathVariable("id") id: String): ResponseEntity<Ingredient?> {
        val optional = ingredientRepo.findById(id)
        return if (optional.isPresent) {
            ResponseEntity.ok(optional.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

}

data class IngredientResource(
        val id: String,
        val name: String,
        val type: Ingredient.Type
) : ResourceSupport() {
    companion object {
        val ingredientAssembler = IngredientResourceAssembler()
        fun from(ingredient: Ingredient) = IngredientResource(ingredient.id,
                ingredient.name,
                ingredient.type)
    }
}

class IngredientResourceAssembler : ResourceAssemblerSupport<Ingredient, IngredientResource>(
        IngredientApiController::class.java, IngredientResource::class.java) {
    override fun instantiateResource(entity: Ingredient) = IngredientResource.from(entity)

    override fun toResource(entity: Ingredient) = createResourceWithId(entity.id, entity)
}
