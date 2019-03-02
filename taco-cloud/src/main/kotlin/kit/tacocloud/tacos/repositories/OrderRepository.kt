package kit.tacocloud.tacos.repositories

import kit.tacocloud.tacos.domain.Order
import org.springframework.data.repository.CrudRepository

interface OrderRepository : CrudRepository<Order, Long> {
//    fun save(order: Order): Order?
}