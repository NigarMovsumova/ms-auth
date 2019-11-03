package az.technical.task.msauth.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfo {
    private String token;
    private String role;
    private String customerId;
    private String email;
}
