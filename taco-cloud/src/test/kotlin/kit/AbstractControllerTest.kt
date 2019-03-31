package kit

import kit.tacos.DiscountCodeProps
import kit.tacos.data.IngredientRepository
import kit.tacos.data.OrderRepository
import kit.tacos.data.TacoRepository
import kit.tacos.data.UserRepository
import kit.tacos.OrderProps
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.crypto.password.PasswordEncoder

abstract class AbstractControllerTest {
    @MockBean
    protected lateinit var ingredientRepository: IngredientRepository
    @MockBean
    protected lateinit var tacoRepository: TacoRepository
    @MockBean
    protected lateinit var orderRepository: OrderRepository
    @MockBean
    protected lateinit var passwordEncoder: PasswordEncoder
    @MockBean
    protected lateinit var userRepository: UserRepository
    @MockBean
    protected lateinit var orderProps: OrderProps
    @MockBean
    protected lateinit var discountProps: DiscountCodeProps
}