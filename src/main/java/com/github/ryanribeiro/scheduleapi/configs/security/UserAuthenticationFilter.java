package com.github.ryanribeiro.scheduleapi.configs.security;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.ryanribeiro.scheduleapi.entities.UserModel;
import com.github.ryanribeiro.scheduleapi.repository.UserRepository;
import com.github.ryanribeiro.scheduleapi.services.JwtTokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Checks if the endpoint requires authentication before processing the request
        if (checkIfEndpointIsNotPublic(request)) {
            String token = recoveryToken(request); // Retrieves the token from the Authorization header of the request
            if (token != null) {
                String subject = jwtTokenService.getSubjectFromToken(token); // Gets the subject (in this case, the username) from the token
                UserModel userModel = userRepository.findByEmail(subject).get(); // Finds the user by email (which is the subject of the token)
                UserDetailsImpl userDetails = new UserDetailsImpl(userModel); // Creates a UserDetails with the found user

                // Creates a Spring Security authentication object
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());

                // Sets the authentication object in the Spring Security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response); // Continues the request processing
    }

    // Retrieves the token from the Authorization header of the request
    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    // Checks if the endpoint requires authentication before processing the request
    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return Arrays.stream(SecurityConfiguration.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED)
                .noneMatch(requestURI::startsWith);
    }

}