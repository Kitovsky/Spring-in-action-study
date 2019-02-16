package kit.tacocloud.tacos.repositories

import kit.tacocloud.tacos.domain.Taco

interface TacoRepository {
    fun save(taco: Taco): Taco
}