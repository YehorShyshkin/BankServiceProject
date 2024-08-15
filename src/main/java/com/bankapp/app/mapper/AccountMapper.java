package com.bankapp.app.mapper;

import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(componentModel = "spring", injectionStrategy = CONSTRUCTOR,
        uses = {AgreementMapper.class})
public interface AccountMapper {
    AccountDTO toDTO(Account account);
    List<AccountDTO> toDTO(List<Account> accountList);
    Account updateAccountFromDTO(AccountDTO accountDTO, @MappingTarget Account account);
}


