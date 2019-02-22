package kit.tacocloud.tacos.web

import kit.tacocloud.logger
import kit.tacocloud.tacos.domain.Ingredient
import kit.tacocloud.tacos.domain.Order
import kit.tacocloud.tacos.domain.Taco
import kit.tacocloud.tacos.repositories.IngredientRepository
import kit.tacocloud.tacos.repositories.TacoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.SessionAttributes
import javax.validation.Valid

@RequestMapping("/design")
@Controller
@SessionAttributes("order")
class DesignTacoController(
        @Autowired private val ingredientRepo: IngredientRepository,
        @Autowired private val tacoRepo: TacoRepository
) {
    companion object {
        val log by logger()
        /*val ingredients = listOf(
                Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),
                Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),
                Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),
                Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN),
                Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES),
                Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),
                Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE),
                Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE),
                Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE),
                Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE)
        )*/
    }

    @ModelAttribute(name = "order")
    fun order() = Order()

    @ModelAttribute(name = "taco")
    fun taco() = Taco()

    @GetMapping
    fun showDesignForm(model: Model): String {
        val ingredients = ingredientRepo.findAll()
        Ingredient.Type.values().forEach { type ->
            model.addAttribute(type.toString().toLowerCase(),
                    ingredients.filter { it.type == type })
        }
//        model.addAttribute("design", Taco())
        return "design"
    }

    @PostMapping
    fun processDesign(@Valid design: Taco, errors: Errors,
                      @ModelAttribute order: Order, model: Model): String {
        if (errors.hasErrors()) {
            return showDesignForm(model)
        }
        log.info("Processing design: {}", design)
        val saved = tacoRepo.save(design)
        order.addTaco(saved)
        return "redirect:/orders/current"
    }
}