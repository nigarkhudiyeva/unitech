package az.unibank.uniTech.v1.controller;

import az.unibank.uniTech.v1.model.AccountViewResponse;
import az.unibank.uniTech.v1.model.CreateAccountReponse;
import az.unibank.uniTech.v1.model.CreateAccountRequest;
import az.unibank.uniTech.v1.model.TransferRequest;
import az.unibank.uniTech.v1.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public CreateAccountReponse create(@AuthenticationPrincipal Long userId, @RequestBody @Valid CreateAccountRequest request) {
        return accountService.create(userId, request);
    }

    @GetMapping
    public List<AccountViewResponse> getAccounts(@AuthenticationPrincipal Long userId) {
        return accountService.getAccounts(userId);
    }


    @PostMapping("transfers")
    public void create(@AuthenticationPrincipal Long userId, @RequestBody @Valid TransferRequest request) {
        accountService.transfer(userId, request);
    }
}
