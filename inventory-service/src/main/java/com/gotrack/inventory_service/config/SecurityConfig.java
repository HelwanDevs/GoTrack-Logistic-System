package com.gotrack.inventory_service.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gotrack.inventory_service.filter.InternalTokenFilter;
import com.gotrack.inventory_service.logging.LoggingAspect;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public InternalTokenFilter internalTokenFilter(JwtKeyConfig jwtKeyConfig) {
        return new InternalTokenFilter(jwtKeyConfig);
    }

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, InternalTokenFilter jwtFilter) throws Exception {

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

                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
