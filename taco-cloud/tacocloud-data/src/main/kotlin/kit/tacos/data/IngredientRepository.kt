package kit.tacos.data

import kit.tacos.domain.Ingredient
import org.springframework.data.repository.CrudRepository

interface IngredientRepository : CrudRepository<Ingredient, String>