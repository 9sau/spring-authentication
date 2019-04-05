package com.saurabh.spring.springauthorizationresourceserver.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.saurabh.spring.springauthorizationresourceserver.custombean.CustomUserDetails;
import com.saurabh.spring.springauthorizationresourceserver.service.CustomUserDetailsService;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	JwtTokenProvider tokenProvider;
	
	@Autowired
	CustomUserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try{
			String token = getJwtFromRequest(request);
			if(StringUtils.hasText(token) && tokenProvider.validateToken(token)){
				String username = tokenProvider.getUsernameFromToken(token);
				CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
			}
		}catch(Exception e){
			
		}
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
			return bearerToken.substring(7);
		return null;
	}

}
