package com.job.jobhunter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public JwtAuthenticationConverter jwtAuthenticationConverter() {
                JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
                grantedAuthoritiesConverter.setAuthorityPrefix("");
                grantedAuthoritiesConverter.setAuthoritiesClaimName("user");
                JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
                return jwtAuthenticationConverter;
        }

        @Bean
        public SecurityFilterChain filterChain(
                HttpSecurity http, CustomAuthenticationEntryPoint customAuthenticationEntryPoint)
                throws Exception {
                http
                        .csrf(csrf -> csrf.disable())
                        .cors(Customizer.withDefaults())
                        .authorizeHttpRequests(
                                authz -> authz
                                        // 1. Public endpoints
                                        .requestMatchers("/", "/api/v1/auth/login", "/api/v1/auth/refresh",
                                                "/storage/**", "/api/v1/auth/register").permitAll()

                                        // 2. WebSocket - Phải khớp với endpoint trong WebSocketConfig
                                        // Nếu trong WebSocketConfig là .addEndpoint("/ws") thì sửa thành "/ws/**"
                                        .requestMatchers("/ws/**").permitAll()

                                        // 3. Public GET APIs
                                        .requestMatchers(HttpMethod.GET, "/api/v1/companies/**", "/api/v1/companies").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/v1/jobs/**", "/api/v1/jobs").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/v1/skills/**", "/api/v1/skills").permitAll()

                                        // 4. Others
                                        .anyRequest().authenticated())

                        .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
                                .authenticationEntryPoint(customAuthenticationEntryPoint))
                        .formLogin(form -> form.disable())
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                return http.build();
        }
}