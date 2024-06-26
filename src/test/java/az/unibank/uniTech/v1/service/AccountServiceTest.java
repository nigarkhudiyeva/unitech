package az.unibank.uniTech.v1.service;

import az.unibank.uniTech.v1.entity.AccountEntity;
import az.unibank.uniTech.v1.entity.AccountStatus;
import az.unibank.uniTech.v1.exception.InsufficientFundsException;
import az.unibank.uniTech.v1.exception.InvalidAccountException;
import az.unibank.uniTech.v1.exception.TransferException;
import az.unibank.uniTech.v1.model.TransferRequest;
import az.unibank.uniTech.v1.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testTransfer_Successful() {
        // Mock data
        AccountEntity fromAccount = createAccountEntity("1234567890123456", AccountStatus.ACTIVE, new BigDecimal("1000"));
        AccountEntity toAccount = createAccountEntity("2345678901234567", AccountStatus.ACTIVE, new BigDecimal("500"));

        TransferRequest request = new TransferRequest("1234567890123456", "2345678901234567", new BigDecimal("300"));

        when(accountRepository.findByUserIdAndIbanAndStatus(any(Long.class), any(String.class), any(AccountStatus.class)))
                .thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIban(any(String.class))).thenReturn(Optional.of(toAccount));

        // Perform transfer
        accountService.transfer(1L, request);

        // Verify balances
        assertEquals(new BigDecimal("700"), fromAccount.getBalance());
        assertEquals(new BigDecimal("800"), toAccount.getBalance());
        verify(accountRepository, times(2)).save(any());
    }

    @Test
    public void testTransfer_whenNoEnoughMoneyOnAccount() {
        AccountEntity fromAccount = createAccountEntity("1234567890123456", AccountStatus.ACTIVE, new BigDecimal("100"));
        AccountEntity toAccount = createAccountEntity("1234567890123457", AccountStatus.ACTIVE, new BigDecimal("200"));

        TransferRequest request = new TransferRequest("1234567890123456", "2345678901234567", new BigDecimal("200"));

        when(accountRepository.findByUserIdAndIbanAndStatus(any(Long.class), any(String.class), any(AccountStatus.class)))
                .thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIban(any(String.class))).thenReturn(Optional.of(toAccount));

        // Perform transfer and assert exception
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class,
                () -> accountService.transfer(1L, request));
        assertEquals("Insufficient funds account balance 100", exception.getMessage()); // Check error message
        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testTransfer_TransferToSameAccountException() {
        // Mock data
        AccountEntity fromAccount = createAccountEntity("1234567890123456", AccountStatus.ACTIVE, new BigDecimal("100"));

        TransferRequest request = new TransferRequest("1234567890123456", "1234567890123456", new BigDecimal("50"));

        when(accountRepository.findByUserIdAndIbanAndStatus(any(Long.class), any(String.class), any(AccountStatus.class)))
                .thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIban(any(String.class))).thenReturn(Optional.of(fromAccount)); // Mock toAccount not found

        // Perform transfer and assert exception
        TransferException exception = assertThrows(TransferException.class,
                () -> accountService.transfer(1L, request));
        assertEquals("You can't transfer to same account", exception.getMessage()); // Check error message
        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testTransfer_TransferToInactiveAccountException() {
        // Mock data
        AccountEntity fromAccount = createAccountEntity("1234567890123456", AccountStatus.ACTIVE, new BigDecimal("100"));
        AccountEntity toAccount = createAccountEntity("2345678901234567", AccountStatus.INACTIVE, new BigDecimal("500"));

        TransferRequest request = new TransferRequest("1234567890123456", "2345678901234567", new BigDecimal("50"));

        when(accountRepository.findByUserIdAndIbanAndStatus(any(Long.class), any(String.class), any(AccountStatus.class)))
                .thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIban(any(String.class))).thenReturn(Optional.of(toAccount));

        // Perform transfer and assert exception
        TransferException exception = assertThrows(TransferException.class,
                () -> accountService.transfer(1L, request));
        assertEquals("2345678901234567 is INACTIVE", exception.getMessage()); // Check error message
        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testTransfer_NonExistingToAccountException() {
        // Mock data
        AccountEntity fromAccount = createAccountEntity("1234567890123456", AccountStatus.ACTIVE, new BigDecimal("100"));

        TransferRequest request = new TransferRequest("1234567890123456", "2345678901234567", new BigDecimal("50"));

        when(accountRepository.findByUserIdAndIbanAndStatus(any(Long.class), any(String.class), any(AccountStatus.class)))
                .thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIban(any(String.class))).thenReturn(Optional.empty()); // Mock toAccount not found

        // Perform transfer and assert exception
        InvalidAccountException exception = assertThrows(InvalidAccountException.class,
                () -> accountService.transfer(1L, request));
        assertEquals("2345678901234567 is not valid", exception.getMessage()); // Check error message
        verify(accountRepository, times(0)).save(any());
    }

    private AccountEntity createAccountEntity(String iban, AccountStatus status, BigDecimal balance) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setIban(iban);
        accountEntity.setStatus(status);
        accountEntity.setBalance(balance);
        return accountEntity;
    }

}