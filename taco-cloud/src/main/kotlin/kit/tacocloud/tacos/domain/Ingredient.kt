package kit.tacocloud.tacos.domain

data class Ingredient(
        val id: String,
        val name: String,
        val type: Type
)

enum class Type {
    WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
}

data class Taco(
        var name: String? = null,
        val ingredients: List<String> = listOf()
)

data class Order(
        var name: String = "",
        var street: String = "",
        var city: String = "",
        var state: String = "",
        var zip: String = "",
        var ccNumber: String = "",
        var ccExpiration: String = "",
        val ccCVV: String = ""
)

