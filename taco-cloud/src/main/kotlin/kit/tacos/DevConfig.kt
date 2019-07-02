package kit.tacos

import kit.tacos.data.IngredientRepository
import kit.tacos.data.PaymentMethodRepository
import kit.tacos.data.TacoRepository
import kit.tacos.data.UserRepository
import kit.tacos.domain.Ingredient
import kit.tacos.domain.PaymentMethod
import kit.tacos.domain.Taco
import kit.tacos.domain.User
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder


@Profile("!prod")
@Configuration
class DevConfig {

    @Bean
    fun dataLoader(repo: IngredientRepository,
                   userRepo: UserRepository,
                   encoder: PasswordEncoder,
                   paymentMethodRepo: PaymentMethodRepository,
                   tacoRepo: TacoRepository): CommandLineRunner {
        // user repo for ease of testing with a built-in user
        return CommandLineRunner {
            val flourTortilla = Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP)
            val cornTortilla = Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP)
            val groundBeef = Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN)
            val carnitas = Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN)
            val tomatoes = Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES)
            val lettuce = Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES)
            val cheddar = Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE)
            val jack = Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE)
            val salsa = Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE)
            val sourCream = Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE)
            repo.save(flourTortilla)
            repo.save(cornTortilla)
            repo.save(groundBeef)
            repo.save(carnitas)
            repo.save(tomatoes)
            repo.save(lettuce)
            repo.save(cheddar)
            repo.save(jack)
            repo.save(salsa)
            repo.save(sourCream)

            val savedUser = userRepo.save(User("test", encoder.encode("test"),
                    "Kitovsky", "123 North Street", "Cross Roads", "TXTEST",
                    "76227", "123-123-1234", "kit-diman@yandex.ru"))

            paymentMethodRepo.save(PaymentMethod(savedUser, "4111111111111111", "321", "10/25"))

            tacoRepo.save(Taco(name = "Carnivore",
                    ingredients = listOf(flourTortilla, groundBeef, carnitas, sourCream, salsa, cheddar)))
            tacoRepo.save(Taco(name = "Bovine Bounty",
                    ingredients = listOf(cornTortilla, groundBeef, cheddar, jack, sourCream)))
            tacoRepo.save(Taco(name = "Veg-Out",
                    ingredients = listOf(flourTortilla, cornTortilla, tomatoes, lettuce, salsa)))
        }
    }
}