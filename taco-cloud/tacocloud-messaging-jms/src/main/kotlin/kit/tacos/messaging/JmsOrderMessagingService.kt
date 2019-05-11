package kit.tacos.messaging

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service
import javax.jms.Message

@Service
class JmsOrderMessagingService(
        @Autowired private val jms: JmsTemplate
) : OrderMessagingService {
    override fun sendOrder(order: Order) =
            jms.convertAndSend("kit.tacocloud.order.queue", order, this::addOrderSource)

    private fun addOrderSource(message: Message): Message = message.apply {
        setStringProperty("X_ORDER_SOURCE", "WEB")
    }
}