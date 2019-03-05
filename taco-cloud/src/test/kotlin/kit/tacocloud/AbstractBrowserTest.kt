package kit.tacocloud

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.springframework.boot.web.server.LocalServerPort
import java.util.concurrent.TimeUnit

abstract class AbstractBrowserTest {
    companion object {
        var browser = HtmlUnitDriver()

        @BeforeAll
        @JvmStatic
        fun setup() {
            browser = HtmlUnitDriver()
            browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
        }

        @AfterAll
        @JvmStatic
        fun tearDown() = browser.quit()
    }

    @LocalServerPort
    protected var port: Int = 0
}