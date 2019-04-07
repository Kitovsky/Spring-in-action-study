package kit.tacos

import kit.tacos.data.TacoRepository
import kit.tacos.domain.Ingredient
import kit.tacos.domain.Taco
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.hateoas.Resource
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.hateoas.mvc.ResourceAssemblerSupport
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.Date

@RestController
@RequestMapping(path = ["/design"], produces = [APPLICATION_JSON_VALUE])
@CrossOrigin(origins = ["*"])
class DesignTacoApiController(
        @Autowired private val tacoRepo: TacoRepository
) {
    companion object {
       private val tacoResourceAssembler = TacoResourceAssembler()
    }

    @GetMapping("/recent")
    fun recentTacos(): Resources<TacoResource> {
        val pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending())
        val resources = tacoResourceAssembler.toResources(tacoRepo.findAll(pageRequest).content)
        val recentResources = Resources<TacoResource>(resources)
        recentResources.add(linkTo(methodOn(this::class.java).recentTacos()).withRel("recents"))
        return recentResources
    }

    @GetMapping("/{id}")
    fun tacoById(@PathVariable("id") id: Long): ResponseEntity<Taco?> {
        val optional = tacoRepo.findById(id)
        return if (optional.isPresent) {
            ResponseEntity.ok(optional.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping(consumes = [APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun postTaco(@RequestBody taco: Taco): Taco {
        return tacoRepo.save(taco)
    }

    private class TacoResourceAssembler()
        : ResourceAssemblerSupport<Taco, TacoResource>(DesignTacoApiController::class.java,
            TacoResource::class.java) {

        override fun instantiateResource(entity: Taco) = TacoResource.from(entity)

        override fun toResource(entity: Taco) = createResourceWithId(entity.id, entity)
    }

    data class TacoResource(
            val name: String,
            val createdAt: Date?,
            val ingredients: List<Ingredient>
    ) : ResourceSupport() {
        companion object {
            fun from(taco: Taco) = TacoResource(taco.name,
                    taco.createdAt,
                    taco.ingredients)
        }
    }
}