package kit.tacos.kitchen

import kit.tacos.Order

interface OrderReceiver {
    fun receiveOrder(): Order?
}