package com.bankapp.app.mapper;

import com.bankapp.app.dto.AgreementDTO;
import com.bankapp.app.model.Agreement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AgreementMapper {
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "accountId", target = "account.id")
    Agreement toEntity(AgreementDTO agreementDTO);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "account.id", target = "accountId")
    AgreementDTO toDto(Agreement agreement);

    void updateAgreementFromDTO(AgreementDTO agreementDTO, @MappingTarget Agreement agreement);
}
