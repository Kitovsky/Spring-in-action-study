package kit.tacos.data

import kit.tacos.domain.Order
import kit.tacos.domain.User
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface OrderRepository : CrudRepository<Order, Long> {
    fun findByUserOrderByPlacedAtDesc(user: User, pageable: Pageable): List<Order>
}