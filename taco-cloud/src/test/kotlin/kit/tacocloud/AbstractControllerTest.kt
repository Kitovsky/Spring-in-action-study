package kit.tacocloud

import kit.tacocloud.tacos.repositories.IngredientRepository
import kit.tacocloud.tacos.repositories.OrderRepository
import kit.tacocloud.tacos.repositories.TacoRepository
import kit.tacocloud.tacos.repositories.UserRepository
import kit.tacocloud.tacos.OrderProps
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
}