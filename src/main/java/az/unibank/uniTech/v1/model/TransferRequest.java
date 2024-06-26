package az.unibank.uniTech.v1.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {

    @NotBlank(message = "Source/From account is required")
    private String from;
    @NotBlank(message = "Target/To Account is required")
    private String to;
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

}
