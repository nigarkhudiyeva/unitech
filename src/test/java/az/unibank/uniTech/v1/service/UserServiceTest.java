package az.unibank.uniTech.v1.service;

import az.unibank.uniTech.v1.entity.UserEntity;
import az.unibank.uniTech.v1.exception.InvalidCredentialsException;
import az.unibank.uniTech.v1.exception.PinAlreadyRegisteredExcetion;
import az.unibank.uniTech.v1.model.CreateUserRequest;
import az.unibank.uniTech.v1.model.CreateUserResponse;
import az.unibank.uniTech.v1.model.LoginUserRequest;
import az.unibank.uniTech.v1.model.LoginUserResponse;
import az.unibank.uniTech.v1.repository.UserRepository;
import org.h2.engine.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtHelper jwtHelper;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser_Successful() {
        // Mock data
        CreateUserRequest request = new CreateUserRequest("123456", "password", "pin");

        when(userRepository.existsByPin(any(String.class))).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());
        // Perform registration
        CreateUserResponse response = userService.create(request);

        // Verify response
        assertNotNull(response);
    }


    @Test
    public void testCreateUser_PinAlreadyRegistered() {
        // Mock data
        CreateUserRequest request = new CreateUserRequest("123456", "password", "123456");

        when(userRepository.existsByPin(any(String.class))).thenReturn(true);

        PinAlreadyRegisteredExcetion exception = assertThrows(PinAlreadyRegisteredExcetion.class,
                () -> userService.create(request));
        assertEquals("123456 already exists", exception.getMessage());
    }

    @Test
    public void testLoginUser_Successful() {
        // Mock data
        LoginUserRequest request = new LoginUserRequest("123456", "password");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setPin("123456");
        userEntity.setPasswordHash("$argon2i$v=19$m=65536,t=2,p=1$kJSLQVZE7o9w6jwGk0W3tg$TYieSWv3U8B6Ecxu9RxgeGa3KINAZSUso+fu0cgQN5A");

        when(userRepository.findByPin(any(String.class))).thenReturn(Optional.of(userEntity));
        when(jwtHelper.generateToken(any(UserEntity.class))).thenReturn("mocked_access_token");

        // Perform login
        LoginUserResponse response = userService.login(request);

        // Verify response
        assertEquals("mocked_access_token", response.getAccessToken());
    }

    @Test
    public void testLoginUser_InvalidCredentials() {
        // Mock data
        LoginUserRequest request = new LoginUserRequest("123456", "wrong_password");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setPin("123456");
        userEntity.setPasswordHash("$argon2i$v=19$m=65536,t=2,p=1$kJSLQVZE7o9w6jwGk0W3tg$TYieSWv3U8B6Ecxu9RxgeGa3KINAZSUso+fu0cgQN5A");

        when(userRepository.findByPin(any(String.class))).thenReturn(Optional.of(userEntity));

        // Perform login and assert exception
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class,
                () -> userService.login(request));
        assertEquals("Pin or Password is invalid", exception.getMessage()); // Check error message
    }
}