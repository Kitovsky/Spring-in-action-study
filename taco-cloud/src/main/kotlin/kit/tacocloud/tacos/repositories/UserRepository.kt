package kit.tacocloud.tacos.repositories

import kit.tacocloud.tacos.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String): User?
}