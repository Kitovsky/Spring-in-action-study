package kit.tacos.kitchen.messaging.jms.listener

import kit.tacos.Order
import kit.tacos.kitchen.KitchenUI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Service

@Profile("jms-listener")
@Service
class OrderListener(
        @Autowired val ui: KitchenUI
) {
    @JmsListener(destination = "kit.tacocloud.order.queue")
    fun receiveOrder(order: Order) = ui.displayOrder(order)
}