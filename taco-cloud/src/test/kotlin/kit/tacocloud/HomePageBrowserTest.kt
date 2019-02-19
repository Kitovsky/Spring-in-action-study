package kit.tacocloud

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.concurrent.TimeUnit

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HomePageBrowserTest {
    companion object {
        val browser = HtmlUnitDriver()

        @BeforeAll
        @JvmStatic
        fun setup() {
            browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
        }

        @AfterAll
        @JvmStatic
        fun tearDown() = browser.quit()
    }

    @LocalServerPort
    private var port: Int = 0

    @Test
    fun homePageTest() {
        val homepage = "http://localhost:$port"
        browser.get(homepage)
        assertEquals("Kitovsky Taco Cloud", browser.title)
        val h1Text = browser.findElementByTagName("h1").text
        assertEquals("Welcome to ...", h1Text)
        val imgSrc = browser.findElementByTagName("img").getAttribute("src")
        assertEquals("$homepage/images/TacoCloud.jpg", imgSrc)
    }
}
