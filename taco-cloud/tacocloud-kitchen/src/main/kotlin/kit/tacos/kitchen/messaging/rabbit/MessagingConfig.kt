package kit.tacos.kitchen.messaging.rabbit

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jms.support.converter.MappingJackson2MessageConverter

@Profile(value = ["rabbitmq-template", "rabbitmq-listener"])
@Configuration
class MessagingConfig {
    @Bean
    fun messageConverter(): MappingJackson2MessageConverter = MappingJackson2MessageConverter()
}