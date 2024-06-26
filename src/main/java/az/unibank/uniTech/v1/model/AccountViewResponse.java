package az.unibank.uniTech.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountViewResponse {
    private String iban;
    private String balance;
    private String status;
}
