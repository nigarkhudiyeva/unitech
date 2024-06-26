package az.unibank.uniTech.v1.service;

import az.unibank.uniTech.v1.entity.AccountEntity;
import az.unibank.uniTech.v1.entity.AccountStatus;
import az.unibank.uniTech.v1.exception.InsufficientFundsException;
import az.unibank.uniTech.v1.exception.InvalidAccountException;
import az.unibank.uniTech.v1.exception.TransferException;
import az.unibank.uniTech.v1.mapper.AccountMapper;
import az.unibank.uniTech.v1.model.AccountViewResponse;
import az.unibank.uniTech.v1.model.CreateAccountReponse;
import az.unibank.uniTech.v1.model.CreateAccountRequest;
import az.unibank.uniTech.v1.model.TransferRequest;
import az.unibank.uniTech.v1.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.iban4j.Iban4jException;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public CreateAccountReponse create(Long userId, CreateAccountRequest request) {
        AccountEntity accountEntity = AccountMapper.INSTANCE.toEntity(request);
        Iban iban = Iban.random(CountryCode.AZ);
        accountEntity.setIban(iban.toString());
        accountEntity.setUserId(userId);
        accountEntity.setStatus(AccountStatus.ACTIVE);
        AccountEntity saved = accountRepository.save(accountEntity);
        return AccountMapper.INSTANCE.toModel(saved);
    }

    public List<AccountViewResponse> getAccounts(Long userId) {
        List<AccountEntity> accountEntities = accountRepository.findAllByUserIdAndStatus(userId, AccountStatus.ACTIVE);
        return AccountMapper.INSTANCE.toView(accountEntities);
    }

    @Transactional
    public void transfer(Long userId, TransferRequest request) {
        AccountEntity fromAccount = accountRepository.findByUserIdAndIbanAndStatus(userId, request.getFrom(), AccountStatus.ACTIVE)
                .orElseThrow(() -> new InvalidAccountException(request.getFrom() + " is not valid"));
        AccountEntity toAccount = accountRepository.findByIban(request.getTo())
                .orElseThrow(() -> new InvalidAccountException(request.getTo() + " is not valid"));

        if(toAccount.getStatus() == AccountStatus.INACTIVE) {
            throw new TransferException(toAccount.getIban() + " is INACTIVE");
        }

        BigDecimal amountToTransfer = request.getAmount();
        BigDecimal fromAccountBalance = fromAccount.getBalance();

        if(fromAccount.getIban().equals(toAccount.getIban())) {
            throw new TransferException("You can't transfer to same account");
        }

        if (fromAccountBalance.compareTo(amountToTransfer) < 0) {
            throw new InsufficientFundsException("Insufficient funds account balance " + fromAccountBalance);
        }

        fromAccount.setBalance(fromAccountBalance.subtract(amountToTransfer));
        toAccount.setBalance(toAccount.getBalance().add(amountToTransfer));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

}
