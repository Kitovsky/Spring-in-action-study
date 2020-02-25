package kit.tacos

import kit.tacos.domain.Ingredient
import kit.tacos.domain.Taco
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.client.Traverson
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.net.URI

@RestController
@RequestMapping("/test")
class TraversonTestController(
        private val traverson: Traverson =
                Traverson(URI.create("http://localhost:8085/api"), MediaTypes.HAL_JSON),
        @Autowired private val rest: RestTemplate
) {

    @PostMapping("/ingredients")
    fun testIngredients(): Any {
        val ingredientRes = traverson
                .follow("ingredients")
                .toObject(typeReference<CollectionModel<Ingredient>>())
        return ingredientRes.content
    }

    @PostMapping("/tacos")
    fun testTacos(): Any {
        val ingredientRes = traverson
                .follow("tacos")
                .toObject(typeReference<CollectionModel<Taco>>())
        return ingredientRes.content
    }

    @PostMapping("/ingredients/add")
    fun testAddIngredient(): Any? {
        val ingredientsUrl = traverson
                .follow("ingredients")
                .asLink().href
        return rest.postForObject(ingredientsUrl,
                Ingredient("testID", "testName", Ingredient.Type.CHEESE),
                Ingredient::class.java)
    }


}


inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}