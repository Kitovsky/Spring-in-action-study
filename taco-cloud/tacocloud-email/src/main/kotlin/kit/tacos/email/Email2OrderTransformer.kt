package kit.tacos.email

import org.apache.commons.text.similarity.LevenshteinDistance
import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer
import org.springframework.integration.support.MessageBuilder
import org.springframework.stereotype.Service
import javax.mail.Message
import javax.mail.internet.InternetAddress

/**
 * <p>Handles email content as taco orders where...</p>
 *  <li> The order's email is the sender's email</li>
 *  <li> The email subject line *must* be "TACO ORDER" or else it will be ignored</li>
 *  <li> Each line of the email starts with the name of a taco design, followed by a colon,
 *    followed by one or more ingredient names in a comma-separated list.</li>
 *
 * <p>The ingredient names are matched against a known set of ingredients using a LevenshteinDistance
 * algorithm. As an example "beef" will match "GROUND BEEF" and be mapped to "GRBF"; "corn" will
 * match "Corn Tortilla" and be mapped to "COTO".</p>
 *
 * <p>An example email body might look like this:</p>
 *
 * <code>
 * Corn Carnitas: corn, carnitas, lettuce, tomatoes, cheddar<br/>
 * Veggielicious: flour, tomatoes, lettuce, salsa
 * </code>
 *
 * <p>This will result in an order with two tacos where the names are "Corn Carnitas" and "Veggielicious".
 * The ingredients will be {COTO, CARN, LETC, TMTO, CHED} and {FLTO,TMTO,LETC,SLSA}.</p>
 */
@Service
class Email2OrderTransformer : AbstractMailMessageTransformer<Order>() {

    companion object {
        const val SUBJECT_KEYWORD = "TACO ORDER"
        val ALL_INGREDIENTS = listOf(
                Ingredient("FLTO", "FLOUR TORTILLA"),
                Ingredient("COTO", "CORN TORTILLA"),
                Ingredient("GRBF", "GROUND BEEF"),
                Ingredient("CARN", "CARNITAS"),
                Ingredient("TMTO", "TOMATOES"),
                Ingredient("LETC", "LETTUCE"),
                Ingredient("CHED", "CHEDDAR"),
                Ingredient("JACK", "MONTERREY JACK"),
                Ingredient("SLSA", "SALSA"),
                Ingredient("SRCR", "SOUR CREAM")
        )
    }

    override fun doTransform(mailMessage: Message) = MessageBuilder.withPayload(processPayload(mailMessage))

    private fun processPayload(mailMessage: Message): Order {
        try {
            with(mailMessage) {
                if (subject.toUpperCase().contains(SUBJECT_KEYWORD)) {
                    val email = from[0] as InternetAddress
                    return parseEmail2Order(email.address, content.toString())
                }
            }
        } catch (oO: Exception) {
            //do nothing
        }
        return Order.EMPTY
    }

    private fun parseEmail2Order(email: String, content: String): Order {
        val order = Order(email)
        for (line in content.split("\\r?\\n")) {
            if (line.isNotBlank() && line.contains(':')) {
                val lineParts = line.split(':')
                val tacoName = lineParts[0].trim()
                val ingredients = lineParts[1].trim()
                val ingredientCodes = mutableListOf<String>()
                for (ingredientName in ingredients.split(',')) {
                    val code = lookupIngredientCode(ingredientName.trim().toUpperCase())
                    if (code != null) {
                        ingredientCodes.add(code)
                    }
                }
                order.addTaco(Taco(tacoName, ingredientCodes))
            }
        }
        return order
    }

    private fun lookupIngredientCode(ingredientName: String): String? {
        for (ingredient in ALL_INGREDIENTS) {
            if (LevenshteinDistance.getDefaultInstance().apply(ingredientName, ingredient.name) < 3
                    || ingredientName.contains(ingredient.name)
                    || ingredient.name.contains(ingredientName)) {
                return ingredient.code
            }
        }
        return null
    }
}