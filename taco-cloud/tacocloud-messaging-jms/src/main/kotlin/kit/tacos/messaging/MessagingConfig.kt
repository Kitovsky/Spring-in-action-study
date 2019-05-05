package kit.tacos.messaging

import kit.tacos.domain.Order
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.support.converter.MappingJackson2MessageConverter

@Configuration
class MessagingConfig {
    @Bean
    fun messageConverter(): MappingJackson2MessageConverter = MappingJackson2MessageConverter().apply {
        setTypeIdPropertyName("_typeId")
        setTypeIdMappings(mapOf<String, Class<*>>("order" to Order::class.java))
    }
}