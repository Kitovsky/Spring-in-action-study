package kit.tacos.kitchen.messaging.jms

import kit.tacos.Order
import kit.tacos.kitchen.OrderReceiver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service

@Profile("jms-template")
@Service("templateOrderReceiver")
class JmsOrderReceiver(
        @Autowired private val jms: JmsTemplate
) : OrderReceiver {
    override fun receiveOrder(): Order? =
            jms.receiveAndConvert("kit.tacocloud.order.queue") as Order?
}