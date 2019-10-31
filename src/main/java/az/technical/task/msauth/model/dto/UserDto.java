package az.technical.task.msauth.model.dto;

import az.technical.task.msauth.security.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private String name;
    private String surname;
    private String password;
    private Role role;
    private LocalDate birthDate;
}
