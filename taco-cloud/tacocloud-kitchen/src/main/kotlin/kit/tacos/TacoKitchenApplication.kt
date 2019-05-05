package kit.tacos

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TacoKitchenApplication

fun main(args: Array<String>) {
    runApplication<TacoKitchenApplication>(*args)
}

inline fun <reified T> T.logger(): Lazy<Logger> {
    return lazy {
        if (T::class.isCompanion) {
            LoggerFactory.getLogger(T::class.java.enclosingClass)
        }
        LoggerFactory.getLogger(T::class.java)
    }
}