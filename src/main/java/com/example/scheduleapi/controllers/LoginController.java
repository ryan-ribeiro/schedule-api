package com.example.scheduleapi.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.scheduleapi.configs.security.jwt.JWTCreator;
import com.example.scheduleapi.configs.security.jwt.JWTObject;
import com.example.scheduleapi.configs.security.jwt.SecurityConfig;
import com.example.scheduleapi.dtos.LoginDto;
import com.example.scheduleapi.dtos.SessaoDto;
import com.example.scheduleapi.models.UserModel;
import com.example.scheduleapi.repositories.UserRepository;
@RestController
public class LoginController {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository repository;

    @PostMapping("/login")
    public ResponseEntity<SessaoDto> logar(@RequestBody LoginDto login){
        UserModel user = repository.findByUsername(login.getUsername());
        if (user == null || !encoder.matches(login.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        SessaoDto sessao = new SessaoDto();
        sessao.setLogin(user.getName());

        JWTObject jwtObject = new JWTObject();
        jwtObject.setIssuedAt(new Date(System.currentTimeMillis()));
        jwtObject.setExpiration(new Date(System.currentTimeMillis() + SecurityConfig.EXPIRATION));
        jwtObject.setRoles(user.getRoles());
        sessao.setToken(JWTCreator.create(SecurityConfig.PREFIX, SecurityConfig.KEY, jwtObject));

        return ResponseEntity.ok(sessao);
    }
}