package kit.tacocloud.tacos.repositories

import kit.tacocloud.tacos.domain.Taco
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreatorFactory
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.sql.Types
import java.util.Date

@Repository
class JdbcTacoRepository(
        @Autowired private val jdbc: JdbcTemplate
) : TacoRepository {

    override fun save(taco: Taco): Taco {
        taco.id = saveTacoInfo(taco)
        taco.ingredients.forEach {
            saveIngredientToTaco(it, taco.id!!)
        }
        return taco
    }

    private fun saveIngredientToTaco(ingredientId: String, tacoId: Long) {
        jdbc.update("insert into Taco_Ingredients (taco, ingredient) values (?, ?)",
                tacoId, ingredientId)
    }

    private fun saveTacoInfo(taco: Taco): Long {
        taco.createdAt = Date()
        val pcs = PreparedStatementCreatorFactory(
                "insert into Taco (name, createdAt) values (?, ?)",
                Types.VARCHAR, Types.TIMESTAMP
        ).newPreparedStatementCreator(arrayOf(taco.name, Timestamp(taco.createdAt!!.time)))
        val keyHolder = GeneratedKeyHolder()
        jdbc.update(pcs, keyHolder)
        return keyHolder.key!!.toLong()
    }
}