package az.unibank.uniTech.v1.repository;

import az.unibank.uniTech.v1.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByPin(String pin);
    Optional<UserEntity> findByPin(String pin);
}
