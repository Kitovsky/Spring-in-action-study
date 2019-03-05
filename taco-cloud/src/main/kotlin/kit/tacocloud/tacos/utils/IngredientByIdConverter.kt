package kit.tacocloud.tacos.utils

import kit.tacocloud.tacos.domain.Ingredient
import kit.tacocloud.tacos.repositories.IngredientRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class IngredientByIdConverter(
        @Autowired private val ingredientRepository: IngredientRepository
) : Converter<String, Ingredient> {
    override fun convert(id: String) = ingredientRepository.findById(id).get()
}