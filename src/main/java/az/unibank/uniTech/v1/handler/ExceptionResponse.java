package az.unibank.uniTech.v1.handler;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExceptionResponse {
    private String message;
    private List<FieldValidationError> validations;
}
