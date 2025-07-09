package com.foodexpress.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
	    	    .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
	    	    .requestMatchers("/api/v1/cart/customer/**","/api/v1/cart/customer/remove","/api/v1/cart/communication/**","/api/v1/order/communication/**","api/v1/customers/{customerId}","/api/v1/auth/register/customer", "/api/v1/auth/register/deliveryAgent","/api/v1/auth/register/restaurantOwner","/api/v1/test","/api/v1/auth/login/admin","/api/v1/auth/login/customer","/api/v1/auth/login/restaurantOwner","/api/v1/auth/login/deliveryAgent","api/v1/auth/delivery_agent/test","api/v1/auth/test2","/actuator/**","/swagger-ui/**","/v3/api-docs/**","/eureka/**","/api/v1/auth/delivery-partners","/api/v1/auth/customers/**","api/v1/communication/**").permitAll()
	            .requestMatchers("/api/v1/admin/**").hasRole("Admin")
	            .requestMatchers("/api/v1/order/customer/**").hasRole("Customer")
	            .requestMatchers("/api/v1/auth/deliveryAgent/**","/api/v1/order/deliveryAgent/**").hasRole("Delivery Agent")
	            .requestMatchers("/api/v1/auth/restaurantOwner/**","/api/v1/order/restaurants/**").hasRole("Restaurant Owner")	            
	            .anyRequest().authenticated())
	            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

	        return http.build();
	    }
	    
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

}
