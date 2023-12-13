package com.bankapp.app.mapper;

import com.bankapp.app.dto.AgreementDTO;
import com.bankapp.app.entity.Agreement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgreementMapper {
//    @Mapping(source = "product.productName", target = "productName")
//    @Mapping(source = "product.productStatus", target = "productStatus")
//    @Mapping(source = "product.currencyCode", target = "currencyCode")
//    @Mapping(source = "product.productInterestRate", target = "productInterestRate")
//    @Mapping(source = "product.productLimit", target = "productLimit")
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
