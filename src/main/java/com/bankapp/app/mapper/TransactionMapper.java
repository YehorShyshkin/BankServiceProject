package com.bankapp.app.mapper;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {

    @Mapping(source = "debitAccount.id", target = "debitAccount")
    @Mapping(source = "creditAccount.id", target = "creditAccount")
    TransactionDTO toDto(Transaction transaction);

    @Mapping(target = "debitAccount.id", source = "debitAccount")
    @Mapping(target = "creditAccount.id", source = "creditAccount")
    Transaction toEntity(TransactionDTO transactionDTO);

    @Mapping(source = "debitAccount", target = "debitAccount.id")
    @Mapping(source = "creditAccount", target = "creditAccount.id")
    Transaction makeTransaction(TransactionDTO transactionDTO);
}
