package com.saurabh.spring.springauthorizationresourceserver.custombean;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.saurabh.spring.springauthorizationresourceserver.entity.User;

public class CustomUserDetails implements UserDetails {

	private Collection<? extends GrantedAuthority> authorities;
	private String password;
	private String username;
	private Long id;

	public CustomUserDetails(Collection<? extends GrantedAuthority> authorities, String password, String username,
			Long id) {
		super();
		this.authorities = authorities;
		this.password = password;
		this.username = username;
		this.id = id;
	}

	public CustomUserDetails(Collection<? extends GrantedAuthority> authorities, String password, String username) {
		super();
		this.authorities = authorities;
		this.password = password;
		this.username = username;
	}

	public CustomUserDetails(User user) {
		username = user.getUsername();
		password = user.getPassword();
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
