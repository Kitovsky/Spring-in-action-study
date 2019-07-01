package kit.tacos

import kit.tacos.data.IngredientRepository
import kit.tacos.data.PaymentMethodRepository
import kit.tacos.data.UserRepository
import kit.tacos.domain.Order
import kit.tacos.domain.Taco
import kit.tacos.dto.EmailOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import java.util.Date
import kotlin.streams.toList

@Service
class EmailOrderService(
        @Autowired private val userRepo: UserRepository,
        @Autowired private val ingredientRepo: IngredientRepository,
        @Autowired private val paymentMethodRepo: PaymentMethodRepository
) {

    fun convertEmail2Order(emailOrder: EmailOrder): Order {
        val userFromMail = userRepo.findByEmail(emailOrder.email) ?: throw HttpClientErrorException(HttpStatus.NOT_FOUND)
        val paymentMethod = paymentMethodRepo.findByUserId(userFromMail.id)
        val order = Order(
                user = userFromMail,
                ccNumber = paymentMethod.ccNumber,
                ccCVV = paymentMethod.ccCVV,
                ccExpiration = paymentMethod.ccExpiration,
                targetName = userFromMail.fullname,
                street = userFromMail.street,
                city = userFromMail.city,
                state = userFromMail.state,
                zip = userFromMail.zip,
                placedAt = Date()
        )
        for (emailTaco in emailOrder.tacos) {
            val ingredients = emailTaco.ingredients.stream()
                    .map(ingredientRepo::findById)
                    .filter { it.isPresent }
                    .map { it.get() }
                    .toList()
            val taco = Taco(
                    name = emailTaco.name,
                    ingredients = ingredients
            )
            order.addTaco(taco)
        }
        return order
    }
}