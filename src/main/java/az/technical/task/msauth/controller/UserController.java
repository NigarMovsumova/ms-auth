package az.technical.task.msauth.controller;

import az.technical.task.msauth.model.dto.UserDto;
import az.technical.task.msauth.model.entity.UserEntity;
import az.technical.task.msauth.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @ApiOperation(value = "Controller for registration")
    @PutMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody UserDto user) {
         service.signUp(user);
    }


}
