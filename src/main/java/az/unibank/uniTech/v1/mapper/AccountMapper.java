package az.unibank.uniTech.v1.mapper;

import az.unibank.uniTech.v1.entity.AccountEntity;
import az.unibank.uniTech.v1.model.AccountViewResponse;
import az.unibank.uniTech.v1.model.CreateAccountReponse;
import az.unibank.uniTech.v1.model.CreateAccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountEntity toEntity(CreateAccountRequest createAccountRequest);
    CreateAccountReponse toModel(AccountEntity accountEntity);
    AccountViewResponse toView(AccountEntity accountEntity);
    List<AccountViewResponse> toView(List<AccountEntity> accountEntities);

}
