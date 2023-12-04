package com.app.travelplan.config;

import com.app.travelplan.security.JwtAuthFilter;
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
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(
            HttpSecurity http,
            JwtAuthFilter jwtAuthFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authentication) ->
                        authentication
                                .requestMatchers("/api/v1/auth/login",
                                        "/api/v1/auth/refresh",
                                        "/api/v1/auth/register" ,
                                        "/error",
                                        "/api/v1/plan/**",
                                        "/api/v1/image/get/**").permitAll()

                                //cho phép sử dụng gét theo chỉ định
                                .requestMatchers(HttpMethod.GET,
                                        "/api/v1/category/**",
                                        "/api/v1/places/**",
                                        "/api/v1/reviews/**",
                                        "/api/v1/link/**",
                                        "/api/v1/user/**",
                                        "/api/v1/vehicle/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/swagger-resources/**").permitAll()
                                .requestMatchers("/v3/api-ui.html").permitAll()
                                .requestMatchers("/webjars/**").permitAll()
                                .requestMatchers("/swagger-ui.html").permitAll()
                                .requestMatchers("/api/v1/admin/**").hasAnyAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated()
                )
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration cfg = new CorsConfiguration();
                            cfg.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                            cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                            cfg.setAllowedHeaders(Arrays.asList("*"));
                            return cfg;
                        })
                )
                .build();
    }
}