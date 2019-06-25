package kit.flow

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@EnableScheduling
@SpringBootApplication
class SimpleFlowApp

fun main(args: Array<String>) {
    runApplication<SimpleFlowApp>(*args)
}

@Component
class TestComponent(
        @Autowired private val fileWriterGateway: FileWriterGateway
) {

    @Scheduled(fixedDelay = 1000L)
    fun write() {
        fileWriterGateway.write2File("yo.txt", "my test message")
    }
}