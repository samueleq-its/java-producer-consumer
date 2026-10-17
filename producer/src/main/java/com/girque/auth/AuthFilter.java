package com.girque.auth;

import java.io.IOException;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthFilter extends OncePerRequestFilter {

	private final String expectedApiKey;

	public AuthFilter(String expectedApiKey) {
		this.expectedApiKey = expectedApiKey;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestApiKey = request.getHeader("X-API-KEY");

		if (expectedApiKey != null && expectedApiKey.equals(requestApiKey)) {
			ApiKeyAuthentication authentication = new ApiKeyAuthentication(requestApiKey,
					AuthorityUtils.NO_AUTHORITIES);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

}
