package kit.tacocloud.tacos.repositories

import kit.tacocloud.tacos.domain.Ingredient

interface IngredientRepository {
    fun findAll(): Iterable<Ingredient>
    fun findById(id: String): Ingredient?
    fun save(ingredient: Ingredient): Ingredient
}