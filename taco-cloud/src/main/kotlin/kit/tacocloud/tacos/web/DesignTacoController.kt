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