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
	    	    		
	    	    .requestMatchers("/api/v1/deliveryAgent/createDeliveryPartner","/api/v1/communication/**","/actuator/**","/swagger-ui/**","/v3/api-docs/**","/eureka/**").permitAll()
	            .requestMatchers("/api/v1/admin/**").hasRole("Admin")
	            .requestMatchers("/api/v1/customer/**").hasRole("Customer")
	            .requestMatchers("/api/v1/deliveryAgent/**").hasRole("Delivery Agent")
	            .requestMatchers("/api/v1/restaurantOwner/**").hasRole("Restaurant Owner")	            
	            .anyRequest().authenticated())
	            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

	        return http.build();
	    }
	    
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

}
