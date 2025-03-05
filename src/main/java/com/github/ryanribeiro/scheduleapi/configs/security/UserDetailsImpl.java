package com.github.ryanribeiro.scheduleapi.configs.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.ryanribeiro.scheduleapi.entities.UserModel;

import lombok.Getter;

@Getter
public class UserDetailsImpl implements UserDetails {
	private UserModel userModel;

    public UserDetailsImpl(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	/*
    	 * This method converts the list of roles associated with the user 
    	 * into a collection of GrantedAuthorities, which is the way Spring Security 
    	 * represents roles. This is done by mapping each role to a new SimpleGrantedAuthority, 
    	 * which is a simple implementation of GrantedAuthority
    	 */
        return userModel.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return userModel.getPassword();
    }

    @Override
    public String getUsername() {
        return userModel.getEmail();  // Intentional mismatch of methods.
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
