package com.printlok.pdp.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.printlok.pdp.security.AuthTokenFilter;
import com.printlok.pdp.security.JwtUtils;
import com.printlok.pdp.services.user.UserServiceImpl;
import com.printlok.pdp.security.AuthEntryPointJwt;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final AuthEntryPointJwt unauthorizedHandler;
	private final JwtUtils jwtUtils;
	private final UserServiceImpl userService;

	public SecurityConfig(AuthEntryPointJwt unauthorizedHandler, JwtUtils jwtUtils, UserServiceImpl userService) {
		super();
		this.unauthorizedHandler = unauthorizedHandler;
		this.jwtUtils = jwtUtils;
		this.userService = userService;
	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter(jwtUtils, userService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.cors(cors -> cors.configurationSource(request -> {
			var corsConfig = new org.springframework.web.cors.CorsConfiguration();
			corsConfig.setAllowedOrigins(List.of("https://localhost:3000")); // Frontend URL
			corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			corsConfig.setAllowedHeaders(List.of("*"));
			corsConfig.setAllowCredentials(true);
			corsConfig.setAllowedOrigins(List.of("https://localhost:3000"));
			corsConfig.setAllowedHeaders(List.of("Content-Type", "Authorization"));
			corsConfig.setExposedHeaders(List.of("Set-Cookie"));

			return corsConfig;
		})).csrf(csrf -> csrf.disable())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/auth/login", "/api/auth/signup", "/api/auth/activate",
								"/api/auth/request-reset", "/api/auth/forgot-password")
						.permitAll().anyRequest().authenticated())
				.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class).build();
	}

}
