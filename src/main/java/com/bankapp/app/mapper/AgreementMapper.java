package com.bankapp.app.mapper;

import com.bankapp.app.dto.AgreementDTO;
import com.bankapp.app.model.Agreement;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgreementMapper {
    AgreementDTO toDTO(Agreement agreement);
    List<AgreementDTO> toDTO(List<Agreement> agreementList);
    Agreement updateAgreementFromDTO(AgreementDTO agreementDTO, @MappingTarget Agreement agreement);
}
