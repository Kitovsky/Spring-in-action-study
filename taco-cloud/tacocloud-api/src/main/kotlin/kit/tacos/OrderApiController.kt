package kit.tacos

import kit.tacos.data.OrderRepository
import kit.tacos.domain.Order
import kit.tacos.dto.EmailOrder
import kit.tacos.messaging.OrderMessagingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.Date

@RestController
@RequestMapping("/orders", consumes = [APPLICATION_JSON_VALUE])
class OrderApiController(
        @Autowired private val orderRepo: OrderRepository,
        @Autowired private val messagingService: OrderMessagingService,
        @Autowired private val emailOrderService: EmailOrderService
) {

    @GetMapping(produces = [APPLICATION_JSON_VALUE])
    fun allOrders(): Flux<Order> = Flux.fromIterable(orderRepo.findAll())

    @PostMapping(consumes = [APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun postOrder(@RequestBody order: Mono<Order>): Mono<Order> {
        order.subscribe(messagingService::sendOrder) // mb replace with doOnNext
        return order.map { orderRepo.save(it) }
    }

    @PostMapping("/fromEmail", consumes = [APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun fromEmail(@RequestBody emailOrder: Mono<EmailOrder>): Mono<Order> {
        val order = emailOrderService.convertEmail2Order(emailOrder)
        order.subscribe(messagingService::sendOrder) // mb replace with doOnNext
        return order.map { orderRepo.save(it) }
    }

    @PutMapping("/{orderId}", consumes = [APPLICATION_JSON_VALUE])
    fun putOrder(@RequestBody order: Mono<Order>): Mono<Order> = order.map { orderRepo.save(it) }

    @PatchMapping("/{orderId}", consumes = [APPLICATION_JSON_VALUE])
    fun patchOrder(@PathVariable("orderId") orderId: Long,
                   @RequestBody patchMono: Mono<OrderDto>): Mono<Order> =
            patchMono.flatMap { patch ->
                Mono.justOrEmpty(orderRepo.findById(orderId))
                        .doOnNext { order -> patch.targetName?.let { order.targetName = it } }
                        .doOnNext { order -> patch.street?.let { order.street = it } }
                        .doOnNext { order -> patch.city?.let { order.city = it } }
                        .doOnNext { order -> patch.state?.let { order.state = it } }
                        .doOnNext { order -> patch.zip?.let { order.zip = it } }
                        .doOnNext { order -> patch.ccNumber?.let { order.ccNumber = it } }
                        .doOnNext { order -> patch.ccExpiration?.let { order.ccExpiration = it } }
                        .doOnNext { order -> patch.ccCVV?.let { order.ccCVV = it } }
                        .map { orderRepo.save(it) }
                        .switchIfEmpty(Mono.error(HttpClientErrorException(HttpStatus.NOT_FOUND)))
            }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOrder(@PathVariable("orderId") orderId: Long) = orderRepo.deleteById(orderId)

    data class OrderDto(
            var id: Long?,
            var placedAt: Date?,
            var targetName: String?,
            var street: String?,
            var city: String?,
            var state: String?,
            var zip: String?,
            var ccNumber: String?,
            var ccExpiration: String?,
            var ccCVV: String?
    )
}

