package az.technical.task.msauth.security.service;


import az.technical.task.msauth.exception.WrongDataException;
import az.technical.task.msauth.model.entity.CustomerEntity;
import az.technical.task.msauth.repository.CustomerRepository;
import az.technical.task.msauth.security.model.SecurityCustomer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SecurityUserService implements UserDetailsService {

    private final CustomerRepository repository;

    public SecurityUserService(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CustomerEntity user = repository
                .findByEmail(username)
                .orElseThrow(() -> new WrongDataException("No such email is registered"));
        return buildSecurityUser(user);
    }

    private SecurityCustomer buildSecurityUser(CustomerEntity user) {
        return SecurityCustomer.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singletonList(user.getRole()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true).build();
    }
}