package az.unibank.uniTech.v1.mapper;

import az.unibank.uniTech.v1.entity.UserEntity;
import az.unibank.uniTech.v1.model.CreateUserRequest;
import az.unibank.uniTech.v1.model.CreateUserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity toEntity(CreateUserRequest createUserRequest);
    CreateUserResponse toModel(UserEntity userEntity);
}
