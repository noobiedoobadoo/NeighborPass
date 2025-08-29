//package com.pranay.apigatewayff.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//@Configuration
//@EnableWebFluxSecurity  // Enable reactive security for WebFlux/Gateway
//public class SecurityConfig {
//
//    @Bean
//    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeExchange(auth -> auth
//                        .pathMatchers("/auth/**").permitAll()
//                        .anyExchange().authenticated()
//                )
//                .httpBasic(httpBasic -> httpBasic.disable())
//                .formLogin(form -> form.disable());
//
//        return http.build();
//    }
//}