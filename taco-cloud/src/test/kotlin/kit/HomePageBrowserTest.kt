package kit

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class HomePageBrowserTest : AbstractBrowserTest() {

    @Test
    internal fun homePageTest() {
        val homepage = "http://localhost:$port"
        browser.get(homepage)
        assertEquals("Kitovsky Taco Cloud", browser.title)
        val h1Text = browser.findElementByTagName("h1").text
        assertEquals("Welcome to ...", h1Text)
        val imgSrc = browser.findElementByTagName("img").getAttribute("src")
        assertEquals("$homepage/images/TacoCloud.jpg", imgSrc)
    }
}
