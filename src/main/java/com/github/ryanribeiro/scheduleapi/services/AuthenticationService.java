package com.github.ryanribeiro.scheduleapi.services;

import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.ryanribeiro.scheduleapi.dtos.LoginUserDto;
import com.github.ryanribeiro.scheduleapi.dtos.SessaoDto;
import com.github.ryanribeiro.scheduleapi.entities.UserModel;

@Service
public class AuthenticationService {

    private static final String LOGIN_URL = "http://localhost:8080/login"; // Ajuste se necessário

    public Authentication autenticarUsuario(UserModel usuario) {
        // Criando a requisição de login
    	LoginUserDto loginRequest = new LoginUserDto(usuario.getEmail(), usuario.getPassword());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginUserDto> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<SessaoDto> response = restTemplate.exchange(
                LOGIN_URL,
                HttpMethod.POST,
                request,
                SessaoDto.class
        );

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Falha ao autenticar usuário");
        }

        // Criando objeto de autenticação e armazenando no contexto do Spring Security
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                usuario.getUsername(), null, Collections.singletonList(new SimpleGrantedAuthority("USERS"))
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        return authToken;
    }
}