package kit.tacocloud.tacos.domain

import org.hibernate.validator.constraints.CreditCardNumber
import org.springframework.format.annotation.DateTimeFormat
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

        @DateTimeFormat(pattern = "dd-MM-yyyy")
        var createdAt: Date? = null,

        @field:NotNull
        @field:Size(min = 5, message = "Name must be at least 5 characters long")
        var name: String? = null,

        @field:Size(min = 1, message = "You must choose at least 1 ingredient")
        var ingredients: List<Ingredient> = listOf()
)

data class Order(
        var id: Long? = null,

        @DateTimeFormat(pattern = "dd-MM-yyyy")
        var placedAt: Date? = null,

        @field:Size(min = 5, max = 50, message = "Name must be from 5 to 50 characters")
        @field:NotBlank(message = "Name is required")
        var name: String = "",

        @field:Size(min = 5, max = 50, message = "Street must be from 5 to 50 characters")
        @field:NotBlank(message = "Street is required")
        var street: String = "",

        @field:Size(min = 5, max = 50, message = "City must be from 5 to 50 characters")
        @field:NotBlank(message = "City is required")
        var city: String = "",

        @field:Size(min = 5, max = 20, message = "State must be from 5 to 20 characters")
        @field:NotBlank(message = "State is required")
        var state: String = "",

        @field:Size(min = 5, max = 10, message = "Zip code must be from 5 to 10 characters")
        @field:NotBlank(message = "Zip code is required")
        var zip: String = "",

        @field:CreditCardNumber(message = "Not a valid credit card number")
        var ccNumber: String = "",

        @field:Pattern(regexp = "^(0[1-9]|1[0-2])/([1-9][0-9])$",
                message = "Must be formatted MM/YY")
        var ccExpiration: String = "",

        @field:Digits(integer = 3, fraction = 0, message = "Invalid CVV")
        var ccCVV: String = "",

        @field:Size(min = 1, message = "You must choose at least 1 taco")
        var tacos: MutableList<Taco> = mutableListOf()
) {
    fun addTaco(taco: Taco) {
        tacos.add(taco)
    }
}

