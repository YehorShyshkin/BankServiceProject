package com.bankapp.app.mapper;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO toDTO(Transaction transaction);

    List<TransactionDTO> toDTO(List<Transaction> transactionList);

    @Mapping(source = "debitAccountId", target = "transactionDebitAccount.id")
    @Mapping(source = "creditAccountId", target = "transactionCreditAccount.id")
    Transaction toTransaction(TransactionDTO transaction);
}
