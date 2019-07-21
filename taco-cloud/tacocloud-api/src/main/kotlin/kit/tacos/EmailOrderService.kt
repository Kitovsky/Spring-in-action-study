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
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.Date

@Service
class EmailOrderService(
        @Autowired private val userRepo: UserRepository,
        @Autowired private val ingredientRepo: IngredientRepository,
        @Autowired private val paymentMethodRepo: PaymentMethodRepository
) {

    fun convertEmail2Order(emailOrderMono: Mono<EmailOrder>): Mono<Order> {
        return emailOrderMono.flatMap { emailOrder ->
            val userFromMailMono = Mono.justOrEmpty(userRepo.findByEmail(emailOrder.email))
                    .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.NOT_FOUND)))
            val paymentMono = userFromMailMono.flatMap { Mono.justOrEmpty(paymentMethodRepo.findByUserId(it.id)) }
            Mono.zip(userFromMailMono, paymentMono)
                    .flatMap {
                        val userFromMail = it.t1
                        val paymentMethod = it.t2
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
                        emailOrderMono.map {
                            for (emailTaco in emailOrder.tacos) {
                                Flux.fromIterable(ingredientRepo.findAllById(emailTaco.ingredients))
                                        .collectList()
                                        .subscribe { ingredients ->
                                            val taco = Taco(
                                                    name = emailTaco.name,
                                                    ingredients = ingredients
                                            )
                                            order.addTaco(taco)
                                        }
                            }
                            order
                        }
                    }
        }
    }
}