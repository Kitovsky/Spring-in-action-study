package kit.tacocloud.tacos.repositories

import kit.tacocloud.tacos.domain.Order
import kit.tacocloud.tacos.domain.User
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface OrderRepository : CrudRepository<Order, Long> {
    fun findByUserOrderByPlacedAtDesc(user: User, pageable: Pageable): List<Order>
}