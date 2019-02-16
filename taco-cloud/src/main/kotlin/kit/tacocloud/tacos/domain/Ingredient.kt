package kit.tacocloud.tacos.domain

import org.hibernate.validator.constraints.CreditCardNumber
import java.util.Date
import javax.validation.constraints.Digits
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class Ingredient(
        val id: String,
        val name: String,
        val type: Type
) {
    enum class Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}

data class Taco(
        var id: Long? = null,
        var createdAt: Date = Date(),
        @field:NotNull
        @field:Size(min = 5, message = "Name must be at least 5 characters long")
        var name: String? = null,
        @field:Size(min = 1, message = "You must choose at least 1 ingredient")
        val ingredients: List<Ingredient> = listOf()
)

data class Order(
        var id: Long? = null,
        var placedAt: Date = Date(),
        @field:NotBlank(message = "Name is required")
        var name: String = "",
        @field:NotBlank(message = "Street is required")
        var street: String = "",
        @field:NotBlank(message = "City is required")
        var city: String = "",
        @field:NotBlank(message = "State is required")
        var state: String = "",
        @field:NotBlank(message = "Zip code is required")
        var zip: String = "",
        @field:CreditCardNumber(message = "Not a valid credit card number")
        var ccNumber: String = "",
        @field:Pattern(regexp = "^(0[1-9]|1[0-2])/([1-9][0-9])$",
                message = "Must be formatted MM/YY")
        var ccExpiration: String = "",
        @field:Digits(integer = 3, fraction = 0, message = "Invalid CVV")
        val ccCVV: String = "",
        @field:Size(min = 1, message = "You must choose at least 1 taco")
        val tacos: MutableList<Taco> = mutableListOf()
) {
    fun addTaco(taco: Taco) {
        tacos.add(taco)
    }
}

