package az.technical.task.msauth.service;

import az.technical.task.msauth.exception.WrongDataException;
import az.technical.task.msauth.model.dto.CustomerDto;
import az.technical.task.msauth.model.entity.CustomerEntity;
import az.technical.task.msauth.repository.CustomerRepository;
import az.technical.task.msauth.security.exceptions.AuthenticationException;
import az.technical.task.msauth.security.model.CustomerInfo;
import az.technical.task.msauth.security.model.Role;
import az.technical.task.msauth.security.service.AuthenticationService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository repository;
    private final AuthenticationService authenticationService;

    public CustomerService(CustomerRepository repository, AuthenticationService authenticationService) {
        this.repository = repository;
        this.authenticationService = authenticationService;
    }

    public void signUp(CustomerDto customerDto) {
        Optional<CustomerEntity> checkedUsername = repository
                .findByUsername(customerDto.getUsername());
        Optional<CustomerEntity> checkedEmail = repository
                .findByEmail(customerDto.getEmail());

        if (checkedUsername.isPresent()) {
            throw new WrongDataException("This username already exists");
        }
        if (checkedEmail.isPresent()) {
            throw new WrongDataException("This email already exists");
        }
        String password = new BCryptPasswordEncoder()
                .encode(customerDto.getPassword());
        CustomerEntity customerEntity = CustomerEntity
                .builder()
                .name(customerDto.getName())
                .surname(customerDto.getSurname())
                .username(customerDto.getUsername())
                .email(customerDto.getEmail())
                .password(password)
                .role(Role.ROLE_USER)
                .birthDate(customerDto.getBirthDate())
                .build();
        repository.save(customerEntity);
    }

    public String getCustomerIdByEmail(String token, String email) {
        CustomerInfo userInfo = authenticationService.validateToken(token);
        String userRole = userInfo.getRole();
        if (!userRole.equals("ROLE_ADMIN")) {
            throw new AuthenticationException("You do not have rights for access");
        }
        CustomerEntity userEntity = repository
                .findByEmail(email)
                .orElseThrow(() -> new WrongDataException("No such email is found"));
        return userEntity.getId().toString();
    }
}
