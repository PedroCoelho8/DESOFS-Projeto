package com.portal.de.pagamentos.config;

import com.portal.de.pagamentos.domain.user.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/public/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/register", "/api/login", "api/verify-2fa").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/teste/token").hasAnyRole(String.valueOf(Role.ADMIN), String.valueOf(Role.RECEIVERENTITY), String.valueOf(Role.SENDERENTITY))
                        .requestMatchers(HttpMethod.POST,"/bankaccounts/**").hasAnyRole(String.valueOf(Role.ADMIN), String.valueOf(Role.RECEIVERENTITY), String.valueOf(Role.SENDERENTITY))
                        .requestMatchers(HttpMethod.POST,"/payments/").hasAnyRole(String.valueOf(Role.ADMIN),String.valueOf(Role.RECEIVERENTITY), String.valueOf(Role.SENDERENTITY))
                        .requestMatchers(HttpMethod.POST,"/payments/send").hasAnyRole(String.valueOf(Role.ADMIN),String.valueOf(Role.SENDERENTITY))
                        .requestMatchers(HttpMethod.POST,"/payments/receive").hasAnyRole(String.valueOf(Role.ADMIN), String.valueOf(Role.RECEIVERENTITY))
                        .requestMatchers(HttpMethod.GET, "/payments", "/payments/").hasAnyRole(String.valueOf(Role.ADMIN))
                        .requestMatchers(HttpMethod.GET, "/payments/user").hasAnyRole(String.valueOf(Role.ADMIN),String.valueOf(Role.RECEIVERENTITY), String.valueOf(Role.SENDERENTITY))
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}