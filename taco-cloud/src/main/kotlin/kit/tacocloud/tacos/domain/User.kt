package kit.tacocloud.tacos.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        private val username: String = "",
        private val password: String = "",
        val fullname: String = "",
        val street: String = "",
        val city: String = "",
        val state: String = "",
        val zip: String = "",
        val phoneNumber: String = ""
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
            mutableListOf(SimpleGrantedAuthority("USER_ROLE"))

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = username

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = password

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}