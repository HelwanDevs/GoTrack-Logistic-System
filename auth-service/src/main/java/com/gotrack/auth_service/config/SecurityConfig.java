package com.gotrack.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gotrack.auth_service.Jwt.JwtFilterImpl;
import com.gotrack.auth_service.Jwt.JwtService;
import com.gotrack.auth_service.Jwt.RefreshTokenService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMongoAuditing
public class SecurityConfig {

    @Bean
    public JwtFilterImpl jwtFilter(JwtService jwtService,
            RefreshTokenService refreshTokenService, UserDetailsService userDetailsService) {
        return new JwtFilterImpl(jwtService, refreshTokenService, userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilterImpl jwtFilter) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, ex1) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");

                            String json = """
                                    {
                                      "status": 403,
                                      "error": "Forbidden",
                                      "message": "%s"
                                    }
                                    """.formatted("User lacks necessary permissions");

                            response.getWriter().write(json);
                        })
                        .authenticationEntryPoint((request, response, ex2) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");

                            String json = """
                                    {
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Authentication required"
                                    }
                                    """;

                            response.getWriter().write(json);
                        }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/logout").permitAll()
                        .requestMatchers("/api/auth/validate").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/auth/refresh-token").hasAnyRole("ADMIN", "EMPLOYEE", "MERCHANT")
                        .requestMatchers(HttpMethod.GET, "/api/auth/accounts").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/auth/accounts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/auth/accounts").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/auth/accounts/{id}")
                        .hasAnyRole("ADMIN", "EMPLOYEE", "MERCHANT")
                        .requestMatchers(HttpMethod.GET, "/api/auth/accounts/**")
                        .hasAnyRole("ADMIN", "EMPLOYEE", "MERCHANT")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
