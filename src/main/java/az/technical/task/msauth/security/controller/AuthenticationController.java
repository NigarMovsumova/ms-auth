package az.technical.task.msauth.security.controller;

import az.technical.task.msauth.security.model.CustomerInfo;
import az.technical.task.msauth.security.model.dto.JwtAuthenticationRequest;
import az.technical.task.msauth.security.model.dto.JwtAuthenticationResponse;
import az.technical.task.msauth.security.service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@CrossOrigin
@Api(value = "Authentication Controller")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("generates token if data is correct")
    @PutMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody JwtAuthenticationRequest request) {
        return service.createAuthenticationToken(request);
    }

    @ApiOperation("if token is valid, returns customer information")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/validate")
    public CustomerInfo validateToken(@RequestHeader("X-Auth-Token") String token) {
        return service.validateToken(token);
    }
}