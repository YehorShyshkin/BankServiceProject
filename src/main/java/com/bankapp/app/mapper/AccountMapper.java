package com.bankapp.app.mapper;

import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(source = "accountName", target = "accountName")
    @Mapping(source = "accountType", target = "accountType")
    @Mapping(source = "accountStatus", target = "accountStatus")
    @Mapping(source = "currencyCode", target = "currencyCode")
    @Mapping(source = "accountBalance", target = "accountBalance")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "client.clientStatus", target = "clientStatus")
    @Mapping(source = "client.firstName", target = "firstName")
    @Mapping(source = "client.lastName", target = "lastName")
/*
    @Mapping(source = "agreementList.agreementSum", target = "agreementSum")
*/
    AccountDTO toDo(Account id);

    List<AccountDTO> toDo(List<Account> accountList);
}
