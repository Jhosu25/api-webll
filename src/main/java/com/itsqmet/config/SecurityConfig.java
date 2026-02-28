package com.itsqmet.config;

import com.itsqmet.component.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/login", "/usuarios/registerUser").permitAll()

                        .requestMatchers(HttpMethod.GET,    "/vinilos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.POST,   "/vinilos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/vinilos/**").hasAnyRole("EMPLEADO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/vinilos/**").hasAnyRole("EMPLEADO", "ADMIN")

                        .requestMatchers(HttpMethod.GET,    "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}n