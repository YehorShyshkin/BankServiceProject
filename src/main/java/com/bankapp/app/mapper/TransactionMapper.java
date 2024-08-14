package com.bankapp.app.mapper;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.model.Transaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO toDTO(Transaction transaction);
    List<TransactionDTO> toDTO(List<Transaction> transactionList);
    Transaction toTransaction(TransactionDTO transaction);
}
