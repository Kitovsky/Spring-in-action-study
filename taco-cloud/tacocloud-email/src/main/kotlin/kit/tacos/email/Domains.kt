package kit.tacos.email

class Order(
        val email: String,
        val tacos: MutableList<Taco> = mutableListOf()
) {
    companion object {
        val EMPTY = Order("")
    }

    fun addTaco(taco: Taco) {
        tacos.add(taco)
    }
}


class Taco(
        val name: String,
        val ingredients: List<String>
)


class Ingredient(
        val code: String,
        val name: String
)