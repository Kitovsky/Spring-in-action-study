package kit.tacos.dto

class EmailOrder(
        val email: String,
        val tacos: List<EmailTaco>
) {
    class EmailTaco(
            val name: String,
            val ingredients: List<String>
    )
}