package kit.tacocloud

import kit.tacocloud.tacos.domain.Ingredient
import kit.tacocloud.tacos.domain.Taco
import kit.tacocloud.tacos.web.DesignTacoController
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import java.util.Optional
import org.mockito.Mockito.`when` as whenever

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
    private var tacoDesign = Taco(name = "Test Taco", ingredients = listOf(ingredients[0], ingredients[2], ingredients[6]))

    @Test
    @WithMockUser(username="test_user", password="test_pass")
    internal fun showDesignFormTest() {
        whenever(ingredientRepository.findAll()).thenReturn(ingredients)
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
    @WithMockUser(username="test_user", password="test_pass", authorities=["ROLE_USER"])
    internal fun processDesignTest() {
        whenever(tacoRepository.save(tacoDesign)).thenReturn(tacoDesign)
        whenever(ingredientRepository.findById("FLTO")).thenReturn(Optional.of(Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP)))
        whenever(ingredientRepository.findById("GRBF")).thenReturn(Optional.of(Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN)))
        whenever(ingredientRepository.findById("CHED")).thenReturn(Optional.of(Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE)))
        mockMvc.perform(post("/design")
                .content("name=Test+Taco&ingredients=FLTO,GRBF,CHED")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection)
                .andExpect(header().stringValues("Location", "/orders/current"))
    }
}