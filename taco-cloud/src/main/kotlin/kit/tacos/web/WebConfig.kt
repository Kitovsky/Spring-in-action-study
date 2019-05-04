package kit.tacos.web

import kit.tacos.domain.Taco
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.hateoas.EntityLinks
import org.springframework.hateoas.PagedResources
import org.springframework.hateoas.Resource
import org.springframework.hateoas.ResourceProcessor
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/").setViewName("home")
        registry.addViewController("/login")
    }

//    @Bean
//    fun tacoProcessor(links: EntityLinks) = ResourceProcessor<PagedResources<Resource<Taco>>> { resource ->
//        resource.add(
//                links.linkFor(Taco::class.java).slash(" recent").withRel("recents")
//        )
//        return@ResourceProcessor resource
//    }

    @Bean
    fun restTemplate(): RestTemplate = RestTemplateBuilder().build()
}