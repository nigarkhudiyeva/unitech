package az.unibank.uniTech.v1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginUserResponse {
    private String accessToken;
}
