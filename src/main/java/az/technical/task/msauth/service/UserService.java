package az.technical.task.msauth.service;

import az.technical.task.msauth.exception.WrongDataException;
import az.technical.task.msauth.model.dto.UserDto;
import az.technical.task.msauth.model.entity.UserEntity;
import az.technical.task.msauth.repository.UserRepository;
import az.technical.task.msauth.security.exceptions.AuthenticationException;
import az.technical.task.msauth.security.model.Role;
import az.technical.task.msauth.security.model.SecurityUser;
import az.technical.task.msauth.security.model.UserInfo;
import az.technical.task.msauth.security.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;
    private final AuthenticationService authenticationService;


    public void signUp(UserDto userDto) {
        UserEntity checkUsername = repository
                .findByEmail(userDto.getEmail())
                .orElseThrow(()-> new WrongDataException("No such username is registered"));
        UserEntity checkEmail = repository
                .findByEmail(userDto.getEmail())
                .orElseThrow(()-> new WrongDataException("No such email is registered"));;
        if (checkUsername == null && checkEmail == null) {
            String password = new BCryptPasswordEncoder()
                    .encode(userDto.getPassword());
            UserEntity user = new UserEntity();
            user.setName(userDto.getName());
            user.setSurname(userDto.getSurname());
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user.setBirthDate(userDto.getBirthDate());
            user.setPassword(password);
            user.setRole(Role.ROLE_USER);
            repository.save(user);
        } else if (checkUsername != null) {
            throw new WrongDataException("this username already exists");
        } else {
            throw new WrongDataException("this mail already exists");
        }
    }

    public String getCustomerIdByEmail(String token, String email){
        UserInfo userInfo=  authenticationService.validateToken(token);
        String userRole= userInfo.getRole();
        System.out.println(userRole);
        if (!userRole.equals("ROLE_ADMIN")){
            throw new AuthenticationException("You do not have rights for access");
        }
        UserEntity userEntity= repository
                .findByEmail(email)
                .orElseThrow(()-> new WrongDataException("No such email is found"));
        System.out.println("customer id="+ userInfo.getCustomerId());
        return userEntity.getId().toString();
    }
}
