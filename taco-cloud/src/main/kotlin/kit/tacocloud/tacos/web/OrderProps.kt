package kit.tacocloud.tacos.web

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Component
@Validated
@ConfigurationProperties("taco.orders")
class OrderProps {
    /**
     * Sets the maximum number of orders to display in a list.
     */
    @Min(value = 5, message = "must be between 5 and 25")
    @Max(value = 25, message = "must be between 5 and 25")
    var pageSize: Int = 20
}