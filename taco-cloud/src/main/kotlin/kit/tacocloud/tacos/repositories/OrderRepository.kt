package kit.tacocloud.tacos.repositories

import kit.tacocloud.tacos.domain.Order

interface OrderRepository {
    fun save(order: Order): Order
}