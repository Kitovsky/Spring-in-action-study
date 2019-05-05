package kit.tacos.kitchen

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Profile(value = ["jms-template", "rabbitmq-template"])
@Controller
@RequestMapping("/order")
class OrderReceiverController(
        @Autowired private val orderReceiver: OrderReceiver
) {

    @GetMapping("/receive")
    fun receiveOrder(model: Model): String {
        val order = orderReceiver.receiveOrder()
        return if (order != null) {
            model.addAttribute("order", order)
            "receiveOrder"
        } else {
            "noOrder"
        }
    }

}