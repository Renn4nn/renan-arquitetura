package br.com.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/products/*/discontinue").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/stock").hasAnyRole("ADMIN", "ESTOQUE")
                .requestMatchers(HttpMethod.POST, "/sales", "/sales/*/close").hasAnyRole("ADMIN", "VENDEDOR")
                .anyRequest().authenticated())
                .formLogin(form -> form.defaultSuccessUrl("/", true))
                .build();
    }

    @Bean
    UserDetailsService users() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password("{noop}admin123").roles("ADMIN").build(),
                User.withUsername("estoque").password("{noop}estoque123").roles("ESTOQUE").build(),
                User.withUsername("vendedor").password("{noop}vendedor123").roles("VENDEDOR").build());
    }
}
