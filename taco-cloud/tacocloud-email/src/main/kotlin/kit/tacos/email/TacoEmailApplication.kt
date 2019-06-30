package kit.tacos.email

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TacoEmailApplication

fun main(args: Array<String>) {
    runApplication<TacoEmailApplication>(*args)
}