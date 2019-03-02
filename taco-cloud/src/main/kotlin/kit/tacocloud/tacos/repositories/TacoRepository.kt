package kit.tacocloud.tacos.repositories

import kit.tacocloud.tacos.domain.Taco
import org.springframework.data.repository.CrudRepository

interface TacoRepository : CrudRepository<Taco, Long> {
//    fun save(taco: Taco): Taco
}