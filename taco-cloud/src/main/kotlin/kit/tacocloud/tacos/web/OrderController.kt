package kit.tacocloud.tacos.web

import kit.tacocloud.logger
import kit.tacocloud.tacos.domain.Order
import kit.tacocloud.tacos.repositories.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.bind.support.SessionStatus
import javax.validation.Valid

@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
class OrderController(
        @Autowired private val orderRepo: OrderRepository
) {
    companion object {
        val log by logger()
    }

    @GetMapping("/current")
    fun orderForm(model: Model): String {
//        model.addAttribute("order", Order())
        return "orderForm"
    }

    @PostMapping
    fun processOrder(@Valid order: Order, errors: Errors,
                     sessionStatus: SessionStatus): String {
        if (errors.hasErrors()) {
            return "orderForm"
        }
        log.info("Order submitted: {}", order)
        orderRepo.save(order)
        sessionStatus.setComplete()
        return "redirect:/"
    }
}