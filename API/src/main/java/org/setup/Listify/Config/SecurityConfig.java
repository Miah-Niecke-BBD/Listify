package org.setup.Listify.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/index").permitAll()  // Allow public access to login page
                        .anyRequest().authenticated()  // Require authentication for all other requests
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Custom login page URL
                        .permitAll()  // Allow public access to login page
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/home", true)  // Redirect to /home after successful login
                        .failureUrl("/login?error=true")  // Redirect to login page on failure
                );

        return http.build();
    }
}



