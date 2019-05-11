package kit.tacos.messaging

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MappingJackson2MessageConverter

@Configuration
class MessagingConfig {
    @Bean
    fun messageConverter(): MappingJackson2MessageConverter = MappingJackson2MessageConverter()
}