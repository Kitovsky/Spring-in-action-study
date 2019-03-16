package kit.tacocloud.tacos.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.ldapAuthentication()
                .userSearchBase("ou=people")
                .groupSearchBase("ou=groups")
                .userSearchFilter("(uid={0})")
                .groupSearchFilter("(member={0})")
                .passwordCompare()
                .passwordEncoder(BCryptPasswordEncoder(5))
                .passwordAttribute("passcode")
                .and()
                .contextSource()
                .url("ldap://tacocloud.com:389/dc=tacocloud,dc=com")
    }
}