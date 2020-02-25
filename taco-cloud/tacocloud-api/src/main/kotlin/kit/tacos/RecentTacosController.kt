package kit.tacos

import kit.tacos.data.TacoRepository
import kit.tacos.domain.Ingredient
import kit.tacos.domain.Taco
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import java.util.Date

@RepositoryRestController
class RecentTacosController(
        @Autowired val tacoRepo: TacoRepository
) {

    @GetMapping(path = ["/tacos/recent"], produces = ["application/hal+json"])
    fun recentTacos(): ResponseEntity<CollectionModel<TacoResource>> { //TODO make with flux
        val page = PageRequest.of(0, 12, Sort.by("createdAt").descending())
        val tacos = tacoRepo.findAll(page).content
        val recentResources = CollectionModel<TacoResource>(TacoResource.tacoResourceAssembler.toCollectionModel(tacos))
        recentResources.add(linkTo(methodOn(this::class.java).recentTacos()).withRel("recents"))
        return ResponseEntity.ok(recentResources)
    }
}

class TacoResourceAssembler
    : RepresentationModelAssemblerSupport<Taco, TacoResource>(DesignTacoApiController::class.java,
        TacoResource::class.java) {

    override fun instantiateModel(entity: Taco) = TacoResource.from(entity)

    override fun toModel(entity: Taco) = createModelWithId(entity.id, entity)
}

@Relation(value = "taco", collectionRelation = "tacos")
data class TacoResource(
        val id: Long,
        val name: String,
        val createdAt: Date?,
        val ingredients: CollectionModel<IngredientResource>
) : RepresentationModel<TacoResource>() {
    companion object {
        val tacoResourceAssembler = TacoResourceAssembler()
        fun from(taco: Taco) = TacoResource(taco.id,
                taco.name,
                taco.createdAt,
                IngredientResource.ingredientAssembler.toCollectionModel(taco.ingredients))
    }
}

data class IngredientResource(
        val id: String,
        val name: String,
        val type: Ingredient.Type
) : RepresentationModel<IngredientResource>() {
    companion object {
        val ingredientAssembler = IngredientResourceAssembler()
        fun from(ingredient: Ingredient) = IngredientResource(ingredient.id,
                ingredient.name,
                ingredient.type)
    }
}

class IngredientResourceAssembler : RepresentationModelAssemblerSupport<Ingredient, IngredientResource>(
        IngredientApiController::class.java, IngredientResource::class.java) {

    override fun instantiateModel(entity: Ingredient) = IngredientResource.from(entity)

    override fun toModel(entity: Ingredient) = createModelWithId(entity.id, entity)
}

