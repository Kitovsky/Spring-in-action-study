package kit.tacocloud.tacos.repositories

import kit.tacocloud.tacos.domain.Ingredient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JdbcIngredientRepository(
        @Autowired private val jdbc: JdbcTemplate
) : IngredientRepository {

    override fun findAll(): Iterable<Ingredient> =
            jdbc.query("select id, name, type from Ingredient", ::mapRowToIngredient)

    override fun findOne(id: String): Ingredient? =
            jdbc.queryForObject("select id, name, type from Ingredient where id=?",
                    ::mapRowToIngredient, arrayOf(id))

    override fun save(ingredient: Ingredient): Ingredient {
        jdbc.update("insert into Ingredient (id, name, type) values (?, ?, ?)",
                ingredient.id,
                ingredient.name,
                ingredient.type.toString())
        return ingredient
    }

    private fun mapRowToIngredient(rs: ResultSet, rowNum: Int) =
            Ingredient(rs.getString("id"),
                    rs.getString("name"),
                    Ingredient.Type.valueOf(rs.getString("type")))
}