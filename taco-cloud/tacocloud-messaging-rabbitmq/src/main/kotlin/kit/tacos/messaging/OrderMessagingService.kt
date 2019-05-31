package kit.tacos.messaging

import kit.tacos.domain.Order

interface OrderMessagingService {
    fun sendOrder(order: Order)
}