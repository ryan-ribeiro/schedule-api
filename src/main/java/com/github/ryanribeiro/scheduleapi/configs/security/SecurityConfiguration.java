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
	
<<<<<<< HEAD
    // Endpoints que requerem autenticação para serem acessados
    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {
    		"/user",
    		"/user/**",
    		"/tarefa",
            "/tarefa/**",
            "/auth/test"
    };

    // Endpoints que só podem ser acessador por usuários com permissão de cliente
    public static final String [] ENDPOINTS_CUSTOMER = {
            "/users/test/customer",
            "/auth/test/customer"
    };

    // Endpoints que só podem ser acessador por usuários com permissão de administrador
    public static final String [] ENDPOINTS_ADMIN = {
            "/users/test/administrator",
=======
    // AUTHENTICATION REQUIRED ENDPOINTS
    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {
    		"/user",
    		"/user/**",
    		"/task",
            "/task/**",
            "/auth/test"
    };

    // CUSTOMER ENDPOINTS
    public static final String [] ENDPOINTS_CUSTOMER = {
            "/auth/test/customer"
    };

    // ADMIN ENDPOINTS
    public static final String [] ENDPOINTS_ADMIN = {
>>>>>>> 8686dbb (Models, methods, beans, and endpoints rennamed to match an English Language project.)
            "/auth/test/administrator"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
<<<<<<< HEAD
        return httpSecurity.csrf().disable() // Desativa a proteção contra CSRF
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configura a política de criação de sessão como stateless
                .and().authorizeHttpRequests() // Habilita a autorização para as requisições HTTP
                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated()
                .requestMatchers(ENDPOINTS_ADMIN).hasRole("ADMINISTRATOR") // Repare que não é necessário colocar "ROLE" antes do nome, como fizemos na definição das roles
                .requestMatchers(ENDPOINTS_CUSTOMER).hasRole("CUSTOMER")
                .anyRequest().denyAll()
                // Adiciona o filtro de autenticação de usuário que criamos, antes do filtro de segurança padrão do Spring Security
=======
        return httpSecurity.csrf().disable() // Disables CSRF protection
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configures the session creation policy as stateless.
                .and().authorizeHttpRequests() // Enables authorization for HTTP requests.
                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated()
                .requestMatchers(ENDPOINTS_ADMIN).hasRole("ADMINISTRATOR") // Notice that it is not necessary to prefix the name with 'ROLE', as we did when defining the roles.
                .requestMatchers(ENDPOINTS_CUSTOMER).hasRole("CUSTOMER")
                .anyRequest().denyAll()
                // Adds the user authentication filter we created before Spring Security's default security filter.
>>>>>>> 8686dbb (Models, methods, beans, and endpoints rennamed to match an English Language project.)
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
