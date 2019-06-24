package kit.flow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SimpleFlowApp

fun main(args: Array<String>) {
    runApplication<SimpleFlowApp>(*args)
}