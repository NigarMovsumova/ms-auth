package az.technical.task.msauth.controller;

import az.technical.task.msauth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    /*@ApiOperation(value = "Controller for registration")
    @PutMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity signUp(@RequestBody UserDto user) {
        return service.signUp(user);
    }*/


}
