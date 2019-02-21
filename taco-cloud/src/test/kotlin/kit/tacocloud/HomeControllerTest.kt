package kit.tacocloud

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(HomeController::class)
class HomeControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun homePageTest() {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk)
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Welcome to ...")))
    }
}
