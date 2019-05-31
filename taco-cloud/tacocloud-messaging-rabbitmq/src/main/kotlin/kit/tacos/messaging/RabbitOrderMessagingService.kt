package kit.tacos.messaging

import kit.tacos.domain.Order
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RabbitOrderMessagingService(
        @Autowired private val rabbit: RabbitTemplate
) : OrderMessagingService {
    override fun sendOrder(order: Order) =
            rabbit.convertAndSend("kit.tacocloud.order.queue", order, this::addOrderSource)

    private fun addOrderSource(message: Message): Message = message.apply {
        messageProperties.setHeader("X_ORDER_SOURCE", "WEB")
    }
}