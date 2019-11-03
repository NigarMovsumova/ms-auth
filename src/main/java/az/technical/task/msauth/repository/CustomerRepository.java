package az.technical.task.msauth.repository;

import az.technical.task.msauth.model.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    Optional<CustomerEntity> findByEmail(String username);

    Optional<CustomerEntity> findByUsername(String username);
}
