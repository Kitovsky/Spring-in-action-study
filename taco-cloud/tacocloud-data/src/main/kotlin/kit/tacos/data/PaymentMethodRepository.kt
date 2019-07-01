package kit.tacos.data

import kit.tacos.domain.PaymentMethod
import org.springframework.data.repository.CrudRepository

interface PaymentMethodRepository : CrudRepository<PaymentMethod, Long> {
    fun findByUserId(userId: Long): PaymentMethod
}