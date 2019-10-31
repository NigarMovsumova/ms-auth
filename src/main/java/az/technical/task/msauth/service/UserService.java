package az.technical.task.msauth.service;

import az.technical.task.msauth.exception.WrongDataException;
import az.technical.task.msauth.model.dto.UserDto;
import az.technical.task.msauth.model.entity.UserEntity;
import az.technical.task.msauth.repository.UserRepository;
import az.technical.task.msauth.security.model.Role;
import az.technical.task.msauth.security.model.SecurityUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    public UserEntity signUp(UserDto userDto) {
        UserEntity checkUsername = repository.findByEmail(userDto.getEmail());
        UserEntity checkEmail = repository.findByEmail(userDto.getEmail());
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
            return user;
        } else if (checkUsername != null) {
            throw new WrongDataException("this username already exists");
        } else {
            throw new WrongDataException("this mail already exists");
        }
    }

    public boolean checkId(Integer id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SecurityUser) {
            SecurityUser user = (SecurityUser) principal;
            String email = user.getEmail();
            UserEntity currentUser = repository.findByEmail(email);
            if (currentUser.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }


}
