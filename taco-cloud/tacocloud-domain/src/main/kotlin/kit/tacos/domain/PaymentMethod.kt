package kit.tacos.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class PaymentMethod(

        @ManyToOne
        val user: User,
        val ccNumber: String,
        val ccCVV: String,
        val ccExpiration: String,

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0
)
