package az.unibank.uniTech.v1.repository;

import az.unibank.uniTech.v1.entity.AccountEntity;
import az.unibank.uniTech.v1.entity.AccountStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    List<AccountEntity> findAllByUserIdAndStatus(Long userId, AccountStatus status);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AccountEntity> findByUserIdAndIbanAndStatus(Long userId, String iban, AccountStatus status);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AccountEntity> findByIban(String iban);
}
