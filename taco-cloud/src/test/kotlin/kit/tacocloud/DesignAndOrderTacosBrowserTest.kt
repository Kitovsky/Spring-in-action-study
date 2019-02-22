package kit.tacocloud

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.stream.Collectors

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class DesignAndOrderTacosBrowserTest : AbstractBrowserTest() {
    @Autowired
    private lateinit var rest: TestRestTemplate

    @Test
    internal fun designTacoPageHappyPathTest() {
        browser.get(homePageUrl())
        clickDesignTaco()
        assertDesignPageElements()
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA")
        clickBuildAnotherTaco()
        buildAndSubmitATaco("Another Taco", "COTO", "CARN", "JACK", "LETC", "SRCR")
        fillInAndSubmitOrderForm()
        assertEquals(homePageUrl(), browser.currentUrl)
    }

    @Test
    internal fun designTacoPageEmptyOrderInfoTest() {
        browser.get(homePageUrl())
        clickDesignTaco()
        assertDesignPageElements()
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA")
        submitEmptyOrderForm()
        fillInAndSubmitOrderForm()
        assertEquals(homePageUrl(), browser.currentUrl)
    }

    @Test
    internal fun designTacoPageInvalidOrderInfoTest() {
        browser.get(homePageUrl())
        clickDesignTaco()
        assertDesignPageElements()
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA")
        submitInvalidOrderForm()
        fillInAndSubmitOrderForm()
        assertEquals(homePageUrl(), browser.currentUrl)
    }

    private fun submitInvalidOrderForm() {
        assertTrue(browser.currentUrl.startsWith(orderDetailsPageUrl()))
        fillField("input#name", "I")
        fillField("input#street", "1")
        fillField("input#city", "F")
        fillField("input#state", "C")
        fillField("input#zip", "8")
        fillField("input#ccNumber", "1234432112344322")
        fillField("input#ccExpiration", "14/91")
        fillField("input#ccCVV", "1234")
        browser.findElementByCssSelector("form").submit()

        assertEquals(orderDetailsPageUrl(), browser.currentUrl)

        val validationErrors = getValidationErrorTexts()
        assertEquals(4, validationErrors.size)
        assertTrue(validationErrors.contains("Please correct the problems below and resubmit"))
        assertTrue(validationErrors.contains("Not a valid credit card number"))
        assertTrue(validationErrors.contains("Must be formatted MM/YY"))
        assertTrue(validationErrors.contains("Invalid CVV"))
    }

    private fun submitEmptyOrderForm() {
        assertEquals(currentOrderDetailsPageUrl(), browser.currentUrl)
        browser.findElementByCssSelector("form").submit()

        assertEquals(orderDetailsPageUrl(), browser.currentUrl)

        val validationErrors = getValidationErrorTexts()
        assertEquals(9, validationErrors.size)
        assertTrue(validationErrors.contains("Please correct the problems below and resubmit"))
        assertTrue(validationErrors.contains("Name is required"))
        assertTrue(validationErrors.contains("Street is required"))
        assertTrue(validationErrors.contains("City is required"))
        assertTrue(validationErrors.contains("State is required"))
        assertTrue(validationErrors.contains("Zip code is required"))
        assertTrue(validationErrors.contains("Not a valid credit card number"))
        assertTrue(validationErrors.contains("Must be formatted MM/YY"))
        assertTrue(validationErrors.contains("Invalid CVV"))
    }

    private fun getValidationErrorTexts() = browser.findElementsByClassName("validationError")
            .stream()
            .map { it.text }
            .collect(Collectors.toList())


    private fun fillInAndSubmitOrderForm() {
        assertTrue(browser.currentUrl.startsWith(orderDetailsPageUrl()))
        fillField("input#name", "Ima Hungry")
        fillField("input#street", "1234 Culinary Blvd.")
        fillField("input#city", "Foodsville")
        fillField("input#state", "CO")
        fillField("input#zip", "81019")
        fillField("input#ccNumber", "4111111111111111")
        fillField("input#ccExpiration", "10/19")
        fillField("input#ccCVV", "123")
        browser.findElementByCssSelector("form").submit()
    }

    private fun fillField(fieldName: String, value: String) {
        val field = browser.findElementByCssSelector(fieldName)
        field.clear()
        field.sendKeys(value)
    }

    private fun assertDesignPageElements() {
        assertEquals(designPageUrl(), browser.currentUrl)
        val ingredientGroups = browser.findElementsByClassName("ingredient-group")
        assertEquals(5, ingredientGroups.size)

        val wrapGroup = browser.findElementByCssSelector("div.ingredient-group#wraps")
        val wraps = wrapGroup.findElements(By.tagName("div"))
        assertEquals(2, wraps.size)
        assertIngredient(wrapGroup, 0, "FLTO", "Flour Tortilla")
        assertIngredient(wrapGroup, 1, "COTO", "Corn Tortilla")

        val proteinGroup = browser.findElementByCssSelector("div.ingredient-group#proteins")
        val proteins = proteinGroup.findElements(By.tagName("div"))
        assertEquals(2, proteins.size)
        assertIngredient(proteinGroup, 0, "GRBF", "Ground Beef")
        assertIngredient(proteinGroup, 1, "CARN", "Carnitas")

        val cheeseGroup = browser.findElementByCssSelector("div.ingredient-group#cheeses")
        val cheeses = wrapGroup.findElements(By.tagName("div"))
        assertEquals(2, cheeses.size)
        assertIngredient(cheeseGroup, 0, "CHED", "Cheddar")
        assertIngredient(cheeseGroup, 1, "JACK", "Monterrey Jack")

        val veggieGroup = browser.findElementByCssSelector("div.ingredient-group#veggies ")
        val veggies = wrapGroup.findElements(By.tagName("div"))
        assertEquals(2, veggies.size)
        assertIngredient(veggieGroup, 0, "TMTO", "Diced Tomatoes")
        assertIngredient(veggieGroup, 1, "LETC", "Lettuce")

        val sauceGroup = browser.findElementByCssSelector("div.ingredient-group#sauces")
        val sauces = wrapGroup.findElements(By.tagName("div"))
        assertEquals(2, sauces.size)
        assertIngredient(sauceGroup, 0, "SLSA", "Salsa")
        assertIngredient(sauceGroup, 1, "SRCR", "Sour Cream")
    }

    private fun clickBuildAnotherTaco() {
        assertTrue(browser.currentUrl.startsWith(orderDetailsPageUrl()))
        browser.findElementByCssSelector("a[id='another']").click()
    }


    private fun buildAndSubmitATaco(name: String, vararg ingredients: String) {
        assertDesignPageElements()
        ingredients.forEach {
            browser.findElementByCssSelector("input[value='$it']").click()
        }
        browser.findElementByCssSelector("input#name").sendKeys(name)
        browser.findElementByCssSelector("form").submit()

    }

    private fun assertIngredient(ingredientGroup: WebElement, ingredientIdx: Int, id: String, name: String) {
        val elements = ingredientGroup.findElements(By.tagName("div"))
        val ingredient = elements[ingredientIdx]
        assertEquals(id, ingredient.findElement(By.tagName("input")).getAttribute("value"))
        assertEquals(name, ingredient.findElement(By.tagName("span")).text)
    }

    private fun clickDesignTaco() {
        assertEquals(homePageUrl(), browser.currentUrl)
        browser.findElementByCssSelector("a[id='design']").click()
    }

    private fun designPageUrl() = homePageUrl() + "design"

    private fun homePageUrl() = "http://localhost:$port/"

    private fun orderDetailsPageUrl() = homePageUrl() + "orders"

    private fun currentOrderDetailsPageUrl() = homePageUrl() + "orders/current"
}
