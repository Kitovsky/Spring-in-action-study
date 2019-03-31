package kit.tacos.web

import kit.tacos.DiscountCodeProps
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/discounts")
class DiscountController(
        @Autowired private val discountProps: DiscountCodeProps
) {
    @GetMapping
    fun getDiscountCodes(model: Model): String {
        model.addAttribute("codes", discountProps.codes)
        return "discountList"
    }
}