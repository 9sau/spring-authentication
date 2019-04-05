package com.saurabh.spring.springauthorizationresourceserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.saurabh.spring.springauthorizationresourceserver.custombean.CustomUserDetails;
import com.saurabh.spring.springauthorizationresourceserver.entity.User;
import com.saurabh.spring.springauthorizationresourceserver.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username or email : " + username));

		return new CustomUserDetails(user);
	}

}
