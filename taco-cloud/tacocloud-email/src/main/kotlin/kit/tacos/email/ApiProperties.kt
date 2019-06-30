package kit.tacos.email

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties("tacocloud.api")
@Component
class ApiProperties(
        /**
         * Default tacocloud order submit URL.
         */
        var url: String = ""
)