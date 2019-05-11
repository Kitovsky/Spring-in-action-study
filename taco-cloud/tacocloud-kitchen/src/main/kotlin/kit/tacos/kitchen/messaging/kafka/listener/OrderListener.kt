package kit.tacos.kitchen.messaging.kafka.listener

import kit.tacos.Order
import kit.tacos.kitchen.KitchenUI
import kit.tacos.logger
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Profile("kafka-listener")
@Service
class OrderListener(
        @Autowired val ui: KitchenUI
) {

    companion object {
        val log by logger()
    }

    @KafkaListener(topics = ["kit.tacocloud.order.queue"])
    fun handle(order: Order, record: ConsumerRecord<String, Order>) {
        log.info("Received from partition ${record.partition()} with timestamp ${record.timestamp()}")
        ui.displayOrder(order)
    }
}