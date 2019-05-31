package kit.tacos.messaging

import kit.tacos.domain.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaOrderMessagingService(
        @Autowired private val kafkaTemplate: KafkaTemplate<String, Order>
) : OrderMessagingService {
    override fun sendOrder(order: Order) {
        kafkaTemplate.send("kit.tacocloud.order.queue", order)
    }
}