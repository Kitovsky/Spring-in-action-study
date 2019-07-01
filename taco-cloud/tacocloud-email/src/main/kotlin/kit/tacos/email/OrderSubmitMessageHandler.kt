package kit.tacos.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.integration.handler.GenericHandler
import org.springframework.messaging.MessageHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class OrderSubmitMessageHandler(
        @Autowired private val apiProperties: ApiProperties
) : GenericHandler<Order> {

    private val restTemplate: RestTemplate = RestTemplateBuilder().build()

    override fun handle(payload: Order?, headers: MessageHeaders?): Any? {
        restTemplate.postForObject(apiProperties.url, payload, String::class.java)
        return null
    }
}