package kit.tacos.kitchen.messaging.jms

import kit.tacos.Order
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jms.support.converter.MappingJackson2MessageConverter

@Profile(value = ["jms-template", "jms-listener"])
@Configuration
class MessagingConfig {
    @Bean
    fun messageConverter(): MappingJackson2MessageConverter = MappingJackson2MessageConverter().apply {
        setTypeIdPropertyName("_typeId")
        setTypeIdMappings(mapOf<String, Class<*>>("order" to Order::class.java))
    }
}