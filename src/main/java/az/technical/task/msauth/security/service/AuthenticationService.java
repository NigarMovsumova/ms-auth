package az.technical.task.msauth.security.service;

import az.technical.task.msauth.exception.WrongDataException;
import az.technical.task.msauth.model.entity.CustomerEntity;
import az.technical.task.msauth.repository.CustomerRepository;
import az.technical.task.msauth.security.exceptions.AuthenticationException;
import az.technical.task.msauth.security.model.CustomerInfo;
import az.technical.task.msauth.security.model.dto.JwtAuthenticationRequest;
import az.technical.task.msauth.security.model.dto.JwtAuthenticationResponse;
import az.technical.task.msauth.security.util.TokenUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationService {

    private final TokenUtils tokenUtils;
    private final CustomerRepository repository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(TokenUtils tokenUtils,
                                 CustomerRepository repository, AuthenticationManager authenticationManager) {
        this.tokenUtils = tokenUtils;
        this.repository = repository;
        this.authenticationManager = authenticationManager;
    }

    public JwtAuthenticationResponse createAuthenticationToken(JwtAuthenticationRequest request) {

        authenticate(request.getEmail(), request.getPassword());
        CustomerEntity userEntity = repository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new WrongDataException("Email is not registered"));
        String customerId = userEntity.getId().toString();
        String role = userEntity.getRole().toString();
        String token = tokenUtils.generateToken(request.getEmail(), customerId, role);
        return new JwtAuthenticationResponse(token);
    }

    public void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials", e);
        }
    }

    public CustomerInfo validateToken(String token) {
        tokenUtils.isTokenValid(token);
        return tokenUtils.getUserInfoFromToken(token);
    }
}
