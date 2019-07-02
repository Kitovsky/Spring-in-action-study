package kit.tacos.email

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.IntegrationFlows
import org.springframework.integration.dsl.Pollers
import org.springframework.integration.dsl.StandardIntegrationFlow
import org.springframework.integration.mail.dsl.Mail

@Configuration
class FlowConfig {

    @Bean
    fun emailFlowConfig(
            mailProperties: EmailProperties,
            email2OrderTransformer: Email2OrderTransformer,
            orderSubmitMessageHandler: OrderSubmitMessageHandler
    ): StandardIntegrationFlow = IntegrationFlows.from(Mail.imapInboundAdapter(mailProperties.imapUrl)) { it.poller(Pollers.fixedDelay(mailProperties.pollRate)) }
            .transform(email2OrderTransformer)
            .handle(orderSubmitMessageHandler)
            .get()

}