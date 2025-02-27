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
	private UserModel userModel; // Classe de usuário que criamos anteriormente

    public UserDetailsImpl(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /*
         Este método converte a lista de papéis (roles) associados ao usuário 
         em uma coleção de GrantedAuthorities, que é a forma que o Spring Security 
         usa para representar papéis. Isso é feito mapeando cada papel para um 
         novo SimpleGrantedAuthority, que é uma implementação simples de 
         GrantedAuthority
        */
        return userModel.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return userModel.getPassword();
    } // Retorna a credencial do usuário que criamos anteriormente

    @Override
    public String getUsername() {
        return userModel.getEmail();
    } // Retorna o nome de usuário do usuário que criamos anteriormente

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
