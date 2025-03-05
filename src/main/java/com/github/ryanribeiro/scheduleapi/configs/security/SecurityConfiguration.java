package com.github.ryanribeiro.scheduleapi.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

	@Autowired
    private UserAuthenticationFilter userAuthenticationFilter;

	public static final String [] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
	        "/v2/api-docs",
	        "/v3/api-docs/**",
	        "/swagger-resources",
	        "/swagger-resources/**",
	        "/configuration/ui",
	        "/configuration/security",
	        "/swagger-ui/**",
	        "/webjars/**",
	        "/auth/register",
	        "/auth/login"
	};
	
    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {
    		"/user",
    		"/user/**",
    		"/task",
            "/task/**",
            "/auth/test"
    };

    public static final String [] ENDPOINTS_CUSTOMER = {
            "/auth/test/customer"
    };

    public static final String [] ENDPOINTS_ADMIN = {
            "/auth/test/administrator"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf().disable() // Disables CSRF protection
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configures the session creation policy as stateless.
                .and().authorizeHttpRequests() // Enables authorization for HTTP requests.
                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated()
                .requestMatchers(ENDPOINTS_ADMIN).hasRole("ADMINISTRATOR") // Notice that it is not necessary to prefix the name with 'ROLE', as we did when defining the roles.
                .requestMatchers(ENDPOINTS_CUSTOMER).hasRole("CUSTOMER")
                .anyRequest().denyAll()
                // Adds the user authentication filter we created before Spring Security's default security filter.
                .and().addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
