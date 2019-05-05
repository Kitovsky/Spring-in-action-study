package kit.tacos.messaging

import org.springframework.core.annotation.Order

interface OrderMessagingService {
    fun sendOrder(order: Order)
}