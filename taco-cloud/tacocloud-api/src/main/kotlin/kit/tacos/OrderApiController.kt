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
import java.util.Date

@RestController
@RequestMapping("/orders", consumes = [APPLICATION_JSON_VALUE])
class OrderApiController(
        @Autowired private val orderRepo: OrderRepository,
        @Autowired private val messagingService: OrderMessagingService,
        @Autowired private val emailOrderService: EmailOrderService
) {

    @GetMapping(produces = [APPLICATION_JSON_VALUE])
    fun allOrders(): Iterable<Order> = orderRepo.findAll()

    @PostMapping(consumes = [APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun postOrder(@RequestBody order: Order): Order {
        messagingService.sendOrder(order)
        return orderRepo.save(order)
    }

    @PostMapping("/fromEmail", consumes = [APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun fromEmail(@RequestBody emailOrder: EmailOrder) = emailOrderService.convertEmail2Order(emailOrder).let {
        messagingService::sendOrder
        orderRepo::save
    }

    @PutMapping("/{orderId}", consumes = [APPLICATION_JSON_VALUE])
    fun putOrder(@RequestBody order: Order): Order = orderRepo.save(order)

    @PatchMapping("/{orderId}", consumes = [APPLICATION_JSON_VALUE])
    fun patchOrder(@PathVariable("orderId") orderId: Long,
                   @RequestBody patch: OrderDto): Order {
        val order = orderRepo.findById(orderId)
                .orElseThrow { HttpClientErrorException(HttpStatus.NOT_FOUND) }
        patch.targetName?.let { order.targetName = it }
        patch.street?.let { order.street = it }
        patch.city?.let { order.city = it }
        patch.state?.let { order.state = it }
        patch.zip?.let { order.zip = it }
        patch.ccNumber?.let { order.ccNumber = it }
        patch.ccExpiration?.let { order.ccExpiration = it }
        patch.ccCVV?.let { order.ccCVV = it }
        return orderRepo.save(order)
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

