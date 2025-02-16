package com.example.scheduleapi.configs.security.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigDatabase {
	
	private final SecurityDatabaseService securityDatabaseService;

    public WebSecurityConfigDatabase(SecurityDatabaseService securityDatabaseService) {
        this.securityDatabaseService = securityDatabaseService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/").permitAll()
            .requestMatchers("/tarefa").hasAnyRole("USERS", "MANAGERS")
            .requestMatchers("/user").hasAnyRole("USERS", "MANAGERS")
            .anyRequest().authenticated()
        )
        .httpBasic(httpBasic -> {});
        
    return http.build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder)
//            throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(securityDatabaseService)
//                .passwordEncoder(passwordEncoder)
//                .and()
//                .build();
//    }
    
    @Bean
    public AuthenticationManager authenticationManager(SecurityDatabaseService securityDatabaseService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(securityDatabaseService);
        authProvider.setPasswordEncoder(passwordEncoder());
        
        ProviderManager providerManager = new ProviderManager(authProvider);
        return providerManager;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


