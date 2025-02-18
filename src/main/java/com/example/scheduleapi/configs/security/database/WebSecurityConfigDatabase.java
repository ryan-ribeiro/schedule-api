package com.example.scheduleapi.configs.security.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.scheduleapi.configs.security.jwt.JWTFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		  prePostEnabled = true, 
		  securedEnabled = true, 
		  jsr250Enabled = true)
public class WebSecurityConfigDatabase {
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	private final SecurityDatabaseService securityDatabaseService;

    public WebSecurityConfigDatabase(SecurityDatabaseService securityDatabaseService) {
        this.securityDatabaseService = securityDatabaseService;
    }
    
    private static final String[] SWAGGER_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
    };
    
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.headers().frameOptions().disable();
//        http.cors().and().csrf().disable()
//                .addFilterAfter(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests(auth -> auth
//                    .requestMatchers(SWAGGER_WHITELIST).permitAll()
//                    .requestMatchers(HttpMethod.POST, "/login").permitAll()
//                    .requestMatchers(HttpMethod.POST, "/user").permitAll()
//                    .requestMatchers(HttpMethod.POST, "/tarefa").permitAll()
//                    .requestMatchers(HttpMethod.GET, "/user").hasAnyRole("USERS", "MANAGERS")
//                    .requestMatchers(HttpMethod.GET, "/tarefa").hasAnyRole("USERS", "MANAGERS")
//                    .requestMatchers("/managers").hasAnyRole("MANAGERS")
//                    .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        		.addFilterAfter(new JWTFilter(), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
    
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.cors().and().csrf().disable()
                .addFilterAfter(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                .requestMatchers(HttpMethod.POST,"/login").permitAll()
                .requestMatchers(HttpMethod.POST,"/user").permitAll()
                .requestMatchers(HttpMethod.POST,"/tarefa").permitAll()
                .requestMatchers(HttpMethod.GET,"/user").hasAnyRole("USERS","MANAGERS")
                .requestMatchers(HttpMethod.GET,"/tarefa").hasAnyRole("USERS","MANAGERS")
                .requestMatchers("/managers").hasAnyRole("MANAGERS")
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    	http
//        .csrf(csrf -> csrf.disable())
//        .authorizeHttpRequests(auth -> auth
//            .requestMatchers("/").permitAll()
//            .requestMatchers("/tarefa").hasAnyRole("USERS", "MANAGERS")
//            .requestMatchers("/user").hasAnyRole("USERS", "MANAGERS")
//            .anyRequest().authenticated()
//        )
//        .httpBasic(httpBasic -> {});
//        
//    return http.build();
//    }
//    
//    @Bean
//    public AuthenticationManager authenticationManager(SecurityDatabaseService securityDatabaseService) throws Exception{
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(securityDatabaseService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        
//        ProviderManager providerManager = new ProviderManager(authProvider);
//        return providerManager;
//    }
}


