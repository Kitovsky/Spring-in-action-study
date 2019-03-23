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
        assertLandedOnLoginPage()
        doRegistration("test_user", "test_password")
        assertLandedOnLoginPage()
        doLogin("test_user", "test_password")
        assertDesignPageElements()
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA")
        clickBuildAnotherTaco()
        buildAndSubmitATaco("Another Taco", "COTO", "CARN", "JACK", "LETC", "SRCR")
        fillInAndSubmitOrderForm()
        assertEquals(homePageUrl(), browser.currentUrl)
        doLogout()
    }

    @Test
    internal fun designTacoPageEmptyOrderInfoTest() {
        browser.get(homePageUrl())
        clickDesignTaco()
        assertLandedOnLoginPage()
        doRegistration("test_user2", "test_password")
        assertLandedOnLoginPage()
        doLogin("test_user2", "test_password")
        assertDesignPageElements()
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA")
        submitEmptyOrderForm()
        fillInAndSubmitOrderForm()
        assertEquals(homePageUrl(), browser.currentUrl)
        doLogout()
    }

    @Test
    internal fun designTacoPageInvalidOrderInfoTest() {
        browser.get(homePageUrl())
        clickDesignTaco()
        assertLandedOnLoginPage()
        doRegistration("test_user3", "test_password")
        assertLandedOnLoginPage()
        doLogin("test_user3", "test_password")
        assertDesignPageElements()
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA")
        submitInvalidOrderForm()
        fillInAndSubmitOrderForm()
        assertEquals(homePageUrl(), browser.currentUrl)
        doLogout()
    }

    private fun submitInvalidOrderForm() {
        assertTrue(browser.currentUrl.startsWith(orderDetailsPageUrl()))
        fillField("input#targetName", "I23423423")
        fillField("input#street", "123423432")
        fillField("input#city", "Fdsfdsf")
        fillField("input#state", "Csdfsdf")
        fillField("input#zip", "8sdfds")
        fillField("input#ccNumber", "1234432112344322")
        fillField("input#ccExpiration", "14/21")
        fillField("input#ccCVV", "1234")
        browser.findElementByCssSelector("form#orderForm").submit()

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
        assertTrue(validationErrors.contains("Name must be from 5 to 50 characters"))
        assertTrue(validationErrors.contains("Street must be from 5 to 50 characters"))
        assertTrue(validationErrors.contains("City must be from 5 to 50 characters"))
        assertTrue(validationErrors.contains("State must be from 5 to 20 characters"))
        assertTrue(validationErrors.contains("Zip code must be from 5 to 10 characters"))
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
        fillField("input#targetName", "Ima Hungry")
        fillField("input#street", "1234 Culinary Blvd.")
        fillField("input#city", "Foodsville")
        fillField("input#state", "CO555")
        fillField("input#zip", "81019")
        fillField("input#ccNumber", "5209204583866906")
        fillField("input#ccExpiration", "10/19")
        fillField("input#ccCVV", "123")
        browser.findElementByCssSelector("form#orderForm").submit()
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
        browser.findElementByCssSelector("form#designForm").submit()

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

    private fun loginPageUrl() = homePageUrl() + "login"

    private fun registrationPageUrl() = homePageUrl() + "register"

    private fun orderDetailsPageUrl() = homePageUrl() + "orders"

    private fun currentOrderDetailsPageUrl() = homePageUrl() + "orders/current"

    private fun doLogin(username: String, password: String) {
        browser.findElementByCssSelector("input#username").sendKeys(username)
        browser.findElementByCssSelector("input#password").sendKeys(password)
        browser.findElementByCssSelector("form#loginForm").submit()
    }

    private fun doRegistration(username: String, password: String) {
        browser.findElementByLinkText("here").click()
        assertEquals(registrationPageUrl(), browser.currentUrl)
        browser.findElementByName("username").sendKeys(username)
        browser.findElementByName("password").sendKeys(password)
        browser.findElementByName("confirm").sendKeys(password)
        browser.findElementByName("fullname").sendKeys("Test McTest")
        browser.findElementByName("street").sendKeys("1234 Test Street")
        browser.findElementByName("city").sendKeys("Test City")
        browser.findElementByName("state").sendKeys("TX")
        browser.findElementByName("zip").sendKeys("12345")
        browser.findElementByName("phone").sendKeys("123-123-1234")
        browser.findElementByCssSelector("form#registerForm").submit()
    }

    private fun doLogout() {
        browser.findElementByCssSelector("form#logoutForm")?.submit()
    }

    private fun assertLandedOnLoginPage() {
        assertEquals(loginPageUrl(), browser.currentUrl)
    }
}
