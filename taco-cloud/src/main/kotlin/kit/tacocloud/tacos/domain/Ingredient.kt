package kit.tacocloud.tacos.domain

import org.hibernate.validator.constraints.CreditCardNumber
import org.springframework.format.annotation.DateTimeFormat
import java.util.Date
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.PrePersist
import javax.persistence.Table
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
data class Ingredient(
        @Id
        val id: String,
        val name: String,
        @Enumerated(EnumType.STRING)
        val type: Type
) {
    enum class Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}

@Entity
data class Taco(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @DateTimeFormat(pattern = "dd-MM-yyyy")
        var createdAt: Date? = null,

        @field:NotNull
        @field:Size(min = 5, message = "Name must be at least 5 characters long")
        var name: String = "",

        @ManyToMany(targetEntity = Ingredient::class)
        @field:Size(min = 1, message = "You must choose at least 1 ingredient")
        var ingredients: List<Ingredient> = listOf()
) {
    @PrePersist
    fun createAt() {
        this.createdAt = Date()
    }
}

@Entity
@Table(name = "Taco_Order")
data class Order(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @DateTimeFormat(pattern = "dd-MM-yyyy")
        var placedAt: Date? = null,

        @field:Size(min = 5, max = 50, message = "Name must be from 5 to 50 characters")
        var targetName: String = "",

        @field:Size(min = 5, max = 50, message = "Street must be from 5 to 50 characters")
        var street: String = "",

        @field:Size(min = 5, max = 50, message = "City must be from 5 to 50 characters")
        var city: String = "",

        @field:Size(min = 5, max = 20, message = "State must be from 5 to 20 characters")
        var state: String = "",

        @field:Size(min = 5, max = 10, message = "Zip code must be from 5 to 10 characters")
        var zip: String = "",

        @field:CreditCardNumber(message = "Not a valid credit card number")
        var ccNumber: String = "",

        @field:Pattern(regexp = "^(0[1-9]|1[0-2])/([1-9][0-9])$",
                message = "Must be formatted MM/YY")
        var ccExpiration: String = "",

        @field:Digits(integer = 3, fraction = 0, message = "Invalid CVV")
        var ccCVV: String = "",

        @ManyToMany(targetEntity = Taco::class)
        @field:Size(min = 1, message = "You must choose at least 1 taco")
        var tacos: MutableList<Taco> = mutableListOf(),

        @ManyToOne
        var user: User = User()
) {
    fun addTaco(taco: Taco) {
        tacos.add(taco)
    }

    @PrePersist
    fun placedAt() {
        this.placedAt = Date()
    }
}

