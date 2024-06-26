package az.unibank.uniTech.v1.handler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldValidationError {
    private String field;
    private String errorMessage;
}