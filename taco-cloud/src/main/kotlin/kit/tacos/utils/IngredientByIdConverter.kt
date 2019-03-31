package kit.tacos.utils

import kit.tacos.domain.Ingredient
import kit.tacos.data.IngredientRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class IngredientByIdConverter(
        @Autowired private val ingredientRepository: IngredientRepository
) : Converter<String, Ingredient> {
    override fun convert(id: String) = ingredientRepository.findById(id).get()
}