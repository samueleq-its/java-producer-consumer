package com.girque.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${security.api-key:}")
	private String apiKey;

	@Bean
	@ConditionalOnProperty(name = "security.enabled", havingValue = "true", matchIfMissing = true)
	public SecurityFilterChain enabledSecurityFilterChain(HttpSecurity http) {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authReg -> authReg
						.requestMatchers(HttpMethod.POST, "/**")
						.authenticated()
						.requestMatchers(HttpMethod.PUT, "/**")
						.authenticated()
						.requestMatchers(HttpMethod.DELETE, "/**")
						.authenticated()
						.anyRequest().permitAll())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(httpSecConfig -> httpSecConfig
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(new AuthFilter(apiKey), UsernamePasswordAuthenticationFilter.class).build();

	}

	@Bean
	@ConditionalOnProperty(name = "security.enabled", havingValue = "false")
	public SecurityFilterChain disabledSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
				.build();
	}
}
