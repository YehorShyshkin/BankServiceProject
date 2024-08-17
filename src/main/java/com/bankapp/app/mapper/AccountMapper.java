package com.bankapp.app.mapper;

import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(componentModel = "spring", injectionStrategy = CONSTRUCTOR,
        uses = {AgreementMapper.class})
public interface AccountMapper {
    @Mapping(source = "clientId", target = "client.id")
    Account toEntity(AccountDTO accountDTO);

    @Mapping(source = "client.id", target = "clientId")
    AccountDTO toDto(Account account);

    void updateAccountFromDTO(AccountDTO accountDTO, @MappingTarget Account account);
}


