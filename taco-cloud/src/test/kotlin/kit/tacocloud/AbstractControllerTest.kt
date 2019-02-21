package kit.tacocloud

import kit.tacocloud.tacos.repositories.IngredientRepository
import kit.tacocloud.tacos.repositories.TacoRepository
import org.springframework.boot.test.mock.mockito.MockBean

abstract class AbstractControllerTest {
    @MockBean
    protected lateinit var ingredientRepository: IngredientRepository
    @MockBean
    protected lateinit var tacoRepository: TacoRepository
}