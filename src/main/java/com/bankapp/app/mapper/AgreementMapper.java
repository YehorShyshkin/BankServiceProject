package com.bankapp.app.mapper;

import com.bankapp.app.controller.dto.AgreementDTO;
import com.bankapp.app.entity.Agreement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgreementMapper {
    @Mapping(source = "account.accountName", target = "accountName")
    @Mapping(source = "account.accountType", target = "accountType")
    @Mapping(source = "account.accountStatus", target = "accountStatus")
    @Mapping(source = "account.accountBalance", target = "accountBalance")
    @Mapping(source = "account.client.clientStatus", target = "clientStatus")
    @Mapping(source = "account.client.clientLastName", target = "clientLastName")

    AgreementDTO toDTO(Agreement agreement);
    List<AgreementDTO> toDTO(List<Agreement> agreementList);
    Agreement updateAgreementFromDTO(AgreementDTO agreementDTO, @MappingTarget Agreement agreement);
}
