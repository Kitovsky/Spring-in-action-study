package kit.tacos.data

import kit.tacos.domain.Taco
import org.springframework.data.repository.CrudRepository

interface TacoRepository : CrudRepository<Taco, Long>