package az.technical.task.msauth.controller;

import az.technical.task.msauth.model.dto.CustomerDto;
import az.technical.task.msauth.security.model.CustomerInfo;
import az.technical.task.msauth.security.service.AuthenticationService;
import az.technical.task.msauth.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/customer")
@Api("Customer Controller")
public class CustomerController {

    private final CustomerService service;
    private final AuthenticationService authenticationService;

    @ApiOperation("sign up new customer")
    @PutMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody CustomerDto customerDto) {
        service.signUp(customerDto);
    }

    @ApiOperation("get customer info")
    @GetMapping("/info")
    public CustomerInfo getCustomerInfo(@RequestHeader("X-Auth-Token") String token) {
        return authenticationService.validateToken(token);
    }

    @ApiOperation("get customerId by email")
    @GetMapping("/id/by/email/{email}")
    public String getCustomerIdByEmail(
            @RequestHeader("X-Auth-Token") String token,
            @PathVariable(name = "email") String email) {
        return service.getCustomerIdByEmail(token, email);
    }
}
