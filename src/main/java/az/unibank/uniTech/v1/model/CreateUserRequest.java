package az.unibank.uniTech.v1.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "FullName is mandatory")
    private String fullName;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @NotBlank(message = "Pin is mandatory")
    private String pin;
}
