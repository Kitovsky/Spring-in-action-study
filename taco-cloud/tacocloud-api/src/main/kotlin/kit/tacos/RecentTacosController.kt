package kit.tacos

import kit.tacos.data.TacoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping

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

