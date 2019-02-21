package kit.tacocloud

import kit.tacocloud.tacos.domain.Ingredient
import kit.tacocloud.tacos.domain.Taco
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@ExtendWith(SpringExtension::class)
@WebMvcTest(DesignTacoController::class)
internal class DesignTacoControllerTest : AbstractControllerTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc
    private val ingredients = listOf(
            Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),
            Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),
            Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),
            Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN),
            Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES),
            Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),
            Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE),
            Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE),
            Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE),
            Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE))
    private var tacoDesign = Taco(name = "Test Taco", ingredients = listOf("FLTO", "GRBF", "CHED"))

    @Test
    internal fun showDesignFormTest() {
        mockMvc.perform(get("/design"))
                .andExpect(status().isOk)
                .andExpect(view().name("design"))
                .andExpect(model().attribute("wrap", ingredients.subList(0, 2)))
                .andExpect(model().attribute("protein", ingredients.subList(2, 4)))
                .andExpect(model().attribute("veggies", ingredients.subList(4, 6)))
                .andExpect(model().attribute("cheese", ingredients.subList(6, 8)))
                .andExpect(model().attribute("sauce", ingredients.subList(8, 10)))
    }

    @Test
    internal fun processDesignTest() {
        mockMvc.perform(post("/design")
                .content("name=Test+Taco&ingredients=FLTO,GRBF,CHED")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection)
                .andExpect(header().stringValues("Location", "/orders/current"))
    }
}