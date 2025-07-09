package com.menu.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.menu.security.JwtFilter;

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
						.requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**", "/eureka/**",
								"/api/v1/communication/**","/api/v1/order/communication/**")
						.permitAll()

						.requestMatchers("/api/v1/admin/**").hasRole("Admin")

						.requestMatchers("/api/v1/categories/**", "api/v1/menu/item/**")
						.hasAnyRole("Customer", "Restaurant Owner")

						.requestMatchers("/api/v1/customer/**").hasAnyRole("Customer","Restaurant Owner")

						.requestMatchers("/api/v1/auth/deliveryAgent/**").hasRole("Delivery Agent")

						.requestMatchers("/api/v1/restaurants/**", "/api/v1/menu/restaurants/**")
						.hasRole("Restaurant Owner")

						.anyRequest().authenticated())

				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}

	@Bean

	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();

	}



}
