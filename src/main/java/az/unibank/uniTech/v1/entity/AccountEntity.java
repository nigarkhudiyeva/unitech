package az.unibank.uniTech.v1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity {
    @Id
    private String iban;
    private BigDecimal balance;
    private AccountStatus status;
    private Long userId;

    @CreationTimestamp
    private OffsetDateTime created;
    @UpdateTimestamp
    private OffsetDateTime updated;

}
