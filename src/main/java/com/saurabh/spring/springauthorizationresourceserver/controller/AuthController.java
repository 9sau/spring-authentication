package com.saurabh.spring.springauthorizationresourceserver.controller;

import java.net.URI;
import java.util.Collections;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.saurabh.spring.springauthorizationresourceserver.entity.Role;
import com.saurabh.spring.springauthorizationresourceserver.entity.User;
import com.saurabh.spring.springauthorizationresourceserver.exception.AppException;
import com.saurabh.spring.springauthorizationresourceserver.payload.request.LoginRequest;
import com.saurabh.spring.springauthorizationresourceserver.payload.request.SignUpRequest;
import com.saurabh.spring.springauthorizationresourceserver.payload.response.ApiResponse;
import com.saurabh.spring.springauthorizationresourceserver.payload.response.JwtAuthenticationResponse;
import com.saurabh.spring.springauthorizationresourceserver.repository.RoleRepository;
import com.saurabh.spring.springauthorizationresourceserver.repository.UserRepository;
import com.saurabh.spring.springauthorizationresourceserver.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private static Logger logger = LoggerFactory.getLogger(AuthController.class);
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@Valid @RequestBody LoginRequest request) {
		logger.info(request.getPassword());
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwtToken = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			return new ResponseEntity(new ApiResponse(false, "Username is already taken"), HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(request.getEmail())) {
			return new ResponseEntity(new ApiResponse(false, "Email already exists"), HttpStatus.BAD_REQUEST);
		}
		User user = new User(request.getName(), request.getUsername(), request.getEmail(), request.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role role = roleRepository.findByRoleName("ROLE_USER").orElseThrow(() -> new AppException("User Role not set"));

		user.setRoles(Collections.singleton(role));
		User result = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}")
				.buildAndExpand(result.getUsername()).toUri();
		return ResponseEntity.created(location).body(new ApiResponse(true, "User Registered Successfully!"));
	}
}
