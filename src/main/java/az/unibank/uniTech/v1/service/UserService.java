package az.unibank.uniTech.v1.service;

import az.unibank.uniTech.v1.entity.UserEntity;
import az.unibank.uniTech.v1.exception.InvalidCredentialsException;
import az.unibank.uniTech.v1.exception.PinAlreadyRegisteredExcetion;
import az.unibank.uniTech.v1.mapper.UserMapper;
import az.unibank.uniTech.v1.model.CreateUserRequest;
import az.unibank.uniTech.v1.model.CreateUserResponse;
import az.unibank.uniTech.v1.model.LoginUserRequest;
import az.unibank.uniTech.v1.model.LoginUserResponse;
import az.unibank.uniTech.v1.repository.UserRepository;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    final int ITERATIONS = 2;
    final int MEMORY = 65536;
    final int PARALLELISM = 1;

    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final Argon2 argon2 = Argon2Factory.create();

    public CreateUserResponse create(CreateUserRequest request) {
        if(userRepository.existsByPin(request.getPin())) {
            throw new PinAlreadyRegisteredExcetion(request.getPin() + " already exists");
        }
        UserEntity userEntity = UserMapper.INSTANCE.toEntity(request);
        String hash = argon2.hash(ITERATIONS, MEMORY, PARALLELISM, request.getPassword().getBytes());
        userEntity.setPasswordHash(hash);
        UserEntity saved = userRepository.save(userEntity);
        return UserMapper.INSTANCE.toModel(saved);
    }

    public LoginUserResponse login(LoginUserRequest request) {
        UserEntity user = userRepository.findByPin(request.getPin())
                .orElseThrow(() -> new InvalidCredentialsException("Pin or Password is invalid"));

        if(!argon2.verify(user.getPasswordHash(), request.getPassword().getBytes())) {
            throw new InvalidCredentialsException("Pin or Password is invalid");
        }
        String token = jwtHelper.generateToken(user);
        return LoginUserResponse.builder()
                .accessToken(token)
                .build();
    }



}
