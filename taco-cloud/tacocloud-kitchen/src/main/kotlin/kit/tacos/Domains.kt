package kit.tacos

import java.util.Date

data class Ingredient( //default values only for json deserialization
        var name: String = "",
        var type: Type = Type.CHEESE
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