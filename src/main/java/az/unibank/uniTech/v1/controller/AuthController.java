package az.unibank.uniTech.v1.controller;

import az.unibank.uniTech.v1.model.CreateUserRequest;
import az.unibank.uniTech.v1.model.CreateUserResponse;
import az.unibank.uniTech.v1.model.LoginUserRequest;
import az.unibank.uniTech.v1.model.LoginUserResponse;
import az.unibank.uniTech.v1.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.engine.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponse register(@RequestBody @Valid CreateUserRequest request) {
        return userService.create(request);
    }

    @PostMapping("/login")
    public LoginUserResponse register(@RequestBody @Valid LoginUserRequest request) {
        return userService.login(request);
    }

}
