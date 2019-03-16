package kit.tacocloud.tacos.web

import kit.tacocloud.tacos.domain.User
import kit.tacocloud.tacos.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/register")
class RegistrationController(
        @Autowired val userRepo: UserRepository,
        @Autowired val passwordEncoder: PasswordEncoder
) {

    @GetMapping
    fun registerForm() = "registration"

    fun processRegistration(form: RegistrationForm): String {
        userRepo.save(form.toUser(passwordEncoder))
        return "redirect:/login"
    }
}

data class RegistrationForm(
        var username: String,
        var password: String,
        var fullname: String,
        var street: String,
        var city: String,
        var state: String,
        var zip: String,
        var phone: String

) {
    fun toUser(passwordEncoder: PasswordEncoder): User =
            User(username, passwordEncoder.encode(password),
                    fullname, street, city, state, zip, phone)

}
