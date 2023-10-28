package com.bankapp.app.mapper;

import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.entity.Account;
import com.bankapp.app.entity.Agreement;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(componentModel = "spring", injectionStrategy = CONSTRUCTOR, uses = {AgreementMapper.class})
public interface AccountMapper {
    @Mapping(source = "client.clientStatus", target = "clientStatus")
    @Mapping(source = "client.clientLastName", target = "clientLastName")
    @Mapping(target = "agreementInterestRate", ignore = true)
    @Mapping(target = "agreementStatus", ignore = true)
    @Mapping(target = "agreementSum", ignore = true)
    AccountDTO toDTO(Account account);
    @AfterMapping
    default void mapAgreements(@MappingTarget AccountDTO accountDTO, Account account) {
        if (account != null && account.getAgreementList() != null) {
            List<BigDecimal> interestRates = new ArrayList<>();
            List<String> statuses = new ArrayList<>();
            List<BigDecimal> sums = new ArrayList<>();

            for (Agreement agreement : account.getAgreementList()) {
                interestRates.add(agreement.getAgreementInterestRate());
                statuses.add(String.valueOf(agreement.getAgreementStatus()));
                sums.add(agreement.getAgreementSum());
            }

            accountDTO.setAgreementInterestRate(interestRates.toString());
            accountDTO.setAgreementStatus(statuses.toString());
            accountDTO.setAgreementSum(sums.toString());
        }
    }

    List<AccountDTO> toDTO(List<Account> accountList);
}


