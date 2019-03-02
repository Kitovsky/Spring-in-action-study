package kit.tacocloud.tacos.repositories

import kit.tacocloud.tacos.domain.Ingredient
import org.springframework.data.repository.CrudRepository

interface IngredientRepository: CrudRepository<Ingredient, String> {
//    fun findAll(): Iterable<Ingredient>
//    fun findById(id: String): Ingredient?
//    fun save(ingredient: Ingredient): Ingredient
}