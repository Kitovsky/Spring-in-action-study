package kit.tacos.kitchen.messaging.rabbit

import kit.tacos.Order
import kit.tacos.kitchen.OrderReceiver
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("rabbit-template")
@Service("templateOrderReceiver")
class RabbitOrderReceiver(
        @Autowired private val rabbit: RabbitTemplate
) : OrderReceiver {
    override fun receiveOrder(): Order? =
            rabbit.receiveAndConvert("kit.tacocloud.order.queue") as Order?
}