package kit.tacos

import kit.tacos.domain.Ingredient
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer

@Configuration
class SpringDataRestConfig : RepositoryRestConfigurer {
    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {
        config.exposeIdsFor(Ingredient::class.java)
    }
}