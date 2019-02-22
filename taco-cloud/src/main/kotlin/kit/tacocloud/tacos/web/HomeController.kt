package kit.tacocloud.tacos.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
@Deprecated("Use WebConfig")
class HomeController {

    @GetMapping("/")
    fun home() = "home"
}