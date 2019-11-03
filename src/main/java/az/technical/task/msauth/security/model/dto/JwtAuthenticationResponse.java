package az.technical.task.msauth.security.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel(description = "Response DTO projection")
public class JwtAuthenticationResponse {

    @ApiModelProperty(name = "token", notes = "If user is authorized ,the generated token")
    private final String token;

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }
}