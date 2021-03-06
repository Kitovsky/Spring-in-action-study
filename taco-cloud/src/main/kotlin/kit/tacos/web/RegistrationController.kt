package kit.tacos.web

import kit.tacos.domain.User
import kit.tacos.data.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/register")
class RegistrationController(
        @Autowired val userRepo: UserRepository,
        @Autowired val passwordEncoder: PasswordEncoder
) {

    @GetMapping
    fun registerForm() = "registration"

    @PostMapping
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
        var phone: String,
        var email: String

) {
    fun toUser(passwordEncoder: PasswordEncoder): User =
            User(username, passwordEncoder.encode(password),
                    fullname, street, city, state, zip, phone, email)

}
