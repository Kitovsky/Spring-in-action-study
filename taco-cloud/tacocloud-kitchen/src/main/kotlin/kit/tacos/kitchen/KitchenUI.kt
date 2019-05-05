package kit.tacos.kitchen

import kit.tacos.Order
import kit.tacos.logger
import org.springframework.stereotype.Service

@Service
class KitchenUI {
    companion object {
        val log by logger()
    }

    fun displayOrder(order: Order) {
        log.info("Received order: $order")
    }
}