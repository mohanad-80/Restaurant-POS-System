package com.konecta.internship.Restaurant_POS_System.auth.service;


import com.konecta.internship.Restaurant_POS_System.User.entity.User;
import com.konecta.internship.Restaurant_POS_System.User.repository.UserRepository;
import com.konecta.internship.Restaurant_POS_System.User.entity.UserRole;
import com.konecta.internship.Restaurant_POS_System.auth.dto.response.AuthenticationResponse;
import com.konecta.internship.Restaurant_POS_System.auth.dto.request.LoginRequest;
import com.konecta.internship.Restaurant_POS_System.auth.dto.request.RegisterRequest;
import com.konecta.internship.Restaurant_POS_System.auth.exception.AccountNotFoundException;
import com.konecta.internship.Restaurant_POS_System.auth.exception.InvalidCredentialsException;
import com.konecta.internship.Restaurant_POS_System.auth.security.jwt.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.CUSTOMER)
                .created_at(LocalDateTime.now())
                .build();

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account with email " + request.getEmail() + " not found"));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Wrong password");
        }

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
