package org.listify.config;

import org.listify.handler.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationCodeGrantFilter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/login",
            "/oauth2/callback/google",
            "/authentication",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/login", "/oauth2/callback/google", "/authentication", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()

                ).cors(Customizer.withDefaults()) 
                .addFilterBefore(jwtTokenFilter, OAuth2AuthorizationCodeGrantFilter.class)
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

}
