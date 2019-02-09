package kit.tacocloud.tacos.domain

data class Ingredient(
        val id: String,
        val name: String,
        val type: Type
)

enum class Type {
    WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
}

data class Taco (
    var name: String? = null,
    val ingredients: List<String> = listOf()
)

