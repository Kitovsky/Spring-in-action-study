package kit.tacos.email

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("tacocloud.email")
class EmailProperties(
        /**
         * Email user name.
         */
        var username: String,

        /**
         * Password from email box.
         */
        var password: String,

        /**
         * Email service host.
         */
        var host: String,

        /**
         * Mail box name.
         */
        var mailbox: String,

        /**
         * Poll rate for email service (ms). Default value 30s (30000ms).
         */
        var pollRate: Long = 30_000
) {

    val imapUrl: String
        get() = "imaps://$username:$password@$host/$mailbox"
}