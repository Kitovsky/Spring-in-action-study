package kit.tacocloud.tacos.security

import kit.tacocloud.tacos.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserRepositoryUserDetailsService(
        @Autowired val userRepo: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String) =
            userRepo.findByUsername(username)
                    ?: throw UsernameNotFoundException("User '$username' not found")
}