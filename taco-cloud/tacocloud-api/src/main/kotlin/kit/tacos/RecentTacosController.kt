package kit.tacos

import kit.tacos.data.TacoRepository
import kit.tacos.domain.Ingredient
import kit.tacos.domain.Taco
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.Resources
import org.springframework.hateoas.core.Relation
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.hateoas.mvc.ResourceAssemblerSupport
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import java.util.Date

@RepositoryRestController
class RecentTacosController(
        @Autowired val tacoRepo: TacoRepository
) {

    @GetMapping(path = ["/tacos/recent"], produces = ["application/hal+json"])
    fun recentTacos(): ResponseEntity<Resources<TacoResource>> {
        val page = PageRequest.of(0, 12, Sort.by("createdAt").descending())
        val tacos = tacoRepo.findAll(page).content
        val recentResources = Resources<TacoResource>(TacoResource.tacoResourceAssembler.toResources(tacos))
        recentResources.add(linkTo(methodOn(this::class.java).recentTacos()).withRel("recents"))
        return ResponseEntity.ok(recentResources)
    }
}

class TacoResourceAssembler
    : ResourceAssemblerSupport<Taco, TacoResource>(DesignTacoApiController::class.java,
        TacoResource::class.java) {

    override fun instantiateResource(entity: Taco) = TacoResource.from(entity)

    override fun toResource(entity: Taco) = createResourceWithId(entity.id, entity)
}

@Relation(value = "taco", collectionRelation = "tacos")
data class TacoResource(
        val id: Long,
        val name: String,
        val createdAt: Date?,
        val ingredients: List<IngredientResource>
) : ResourceSupport() {
    companion object {
        val tacoResourceAssembler = TacoResourceAssembler()
        fun from(taco: Taco) = TacoResource(taco.id,
                taco.name,
                taco.createdAt,
                IngredientResource.ingredientAssembler.toResources(taco.ingredients))
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

