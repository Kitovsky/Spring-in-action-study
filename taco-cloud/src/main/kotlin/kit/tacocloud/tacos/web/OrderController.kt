package kit.tacocloud.tacos.web

import kit.tacocloud.logger
import kit.tacocloud.tacos.domain.Order
import kit.tacocloud.tacos.domain.User
import kit.tacocloud.tacos.repositories.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.bind.support.SessionStatus
import javax.validation.Valid

@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
class OrderController(
        @Autowired private val orderRepo: OrderRepository,
        @Autowired private val orderProps: OrderProps
) {
    companion object {
        val log by logger()
    }

    @GetMapping("/current")
    fun orderForm(@AuthenticationPrincipal user: User,
                  @ModelAttribute order: Order): String {
        if (order.targetName.isBlank()) order.targetName = user.fullname
        if (order.street.isBlank()) order.street = user.street
        if (order.city.isBlank()) order.city = user.city
        if (order.state.isBlank()) order.state = user.state
        if (order.zip.isBlank()) order.zip = user.zip
        return "orderForm"
    }

    @PostMapping
    fun processOrder(@Valid order: Order, errors: Errors,
                     sessionStatus: SessionStatus,
                     @AuthenticationPrincipal user: User): String {
        if (errors.hasErrors()) {
            return "orderForm"
        }
        log.info("Order submitted: {}", order)
        orderRepo.save(order)
        sessionStatus.setComplete()
        return "redirect:/"
    }

    @GetMapping
    fun ordersForUser(@AuthenticationPrincipal user: User, model: Model): String {
        log.info("Page size '${orderProps.pageSize}' will be used")
        val pageable = PageRequest.of(0, orderProps.pageSize)
        model.addAttribute("orders", orderRepo.findByUserOrderByPlacedAtDesc(user, pageable))
        return "orderList"
    }
}