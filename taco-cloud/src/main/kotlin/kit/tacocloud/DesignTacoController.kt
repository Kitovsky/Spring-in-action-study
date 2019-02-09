package kit.tacocloud

import kit.tacocloud.tacos.domain.Ingredient
import kit.tacocloud.tacos.domain.Taco
import kit.tacocloud.tacos.domain.Type
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/design")
@Controller
class DesignTacoController {
    companion object {
        val log by logger()
        val ingredients = listOf(
                Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                Ingredient("CARN", "Carnitas", Type.PROTEIN),
                Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                Ingredient("LETC", "Lettuce", Type.VEGGIES),
                Ingredient("CHED", "Cheddar", Type.CHEESE),
                Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                Ingredient("SLSA", "Salsa", Type.SAUCE),
                Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        )
    }

    @GetMapping
    fun showDesignForm(model: Model): String {
        Type.values().forEach { type ->
            model.addAttribute(type.toString().toLowerCase(),
                    ingredients.filter { it.type == type })
        }
        model.addAttribute("design", Taco())
        return "design"
    }

    @PostMapping
    fun processDesign(design: Taco): String {
        log.info("Processing design: {}", design)

        return "redirect:/orders/current"
    }
}