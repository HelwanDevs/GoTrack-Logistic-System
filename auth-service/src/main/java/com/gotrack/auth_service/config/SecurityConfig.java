package com.gotrack.auth_service.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.gotrack.auth_service.Jwt.JwtFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMongoAuditing
public class SecurityConfig {
    @Autowired
    JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                        .requestMatchers("/api/auth/refresh-token").permitAll()
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
                "http://localhost:5173" // React
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

}
