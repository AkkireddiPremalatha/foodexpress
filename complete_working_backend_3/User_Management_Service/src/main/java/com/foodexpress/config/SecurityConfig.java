package com.foodexpress.config;
 
 
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
 
import com.foodexpress.security.JwtFilter;
 
import lombok.RequiredArgsConstructor;
 
@Configuration

@RequiredArgsConstructor

@EnableWebSecurity

public class SecurityConfig {

	    private final JwtFilter jwtFilter;
 
	    @Bean

	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	        http
	        .cors(cors->{})
	        .csrf(csrf -> csrf.disable())

	            .authorizeHttpRequests(auth -> auth

	                // Public endpoints

	                .requestMatchers(

	                    "/api/v1/auth/login",

	                    "/api/v1/auth/register/customer",

	                    "/api/v1/auth/register/deliveryAgent",

	                    "/api/v1/auth/register/restaurantOwner",

	                    "/api/v1/test",

	                    "/api/v1/delivery_agent/test",

	                    "/api/v1/test2",

	                    "/actuator/**",

	                    "/swagger-ui/**",

	                    "/v3/api-docs/**",

	                    "/eureka/**",

	                    "/api/v1/auth/customers/**"

	                ).permitAll()
 
	                // Role-based access

	                .requestMatchers("/api/v1/auth/admin/**").hasRole("Admin")

	                .requestMatchers("/api/v1/auth/customer/**").hasRole("Customer")

	                .requestMatchers("/api/v1/auth/deliveryAgent/**").hasRole("Delivery Agent")

	                .requestMatchers("/api/v1/auth/restaurantOwner/**").hasRole("Restaurant Owner")
 
	                // All other requests require authentication

	                .anyRequest().authenticated()

	            )

	            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
 
	        return http.build();

	    }
 
	    @Bean

	    public PasswordEncoder passwordEncoder() {

	        return new BCryptPasswordEncoder();

	    }
 
}

 