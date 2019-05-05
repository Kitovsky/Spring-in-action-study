package kit.tacos

import java.util.Date

data class Ingredient(
        val name: String,
        val type: Type
) {
    enum class Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}

data class Taco(
        var createdAt: Date? = null,
        var name: String = "",
        var ingredients: List<Ingredient> = listOf()
)

data class Order(
        var placedAt: Date? = null,
        var targetName: String = "",
        var street: String = "",
        var city: String = "",
        var state: String = "",
        var zip: String = "",
        var tacos: MutableList<Taco> = mutableListOf()
)