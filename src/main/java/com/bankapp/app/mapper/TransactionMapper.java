package com.bankapp.app.mapper;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
//    @Mapping(source = "debitTransaction", target = "transactionDebitAccount.id")
//    @Mapping(source = "creditTransaction", target = "transactionCreditAccount.id")
//    @Mapping(source = "transactionDebitAccount.accountStatus", target = "debitAccountStatus")
//    @Mapping(source = "transactionDebitAccount.currencyCode", target = "debitAccountCurrencyCode")
//    @Mapping(source = "transactionDebitAccount.accountBalance", target = "debitAccountBalance")
//
//    @Mapping(source = "transactionCreditAccount.accountType", target = "creditAccountType")
//    @Mapping(source = "transactionCreditAccount.accountStatus", target = "creditAccountStatus")
//    @Mapping(source = "transactionCreditAccount.currencyCode", target = "creditAccountCurrencyCode")
//    @Mapping(source = "transactionCreditAccount.accountBalance", target = "creditAccountBalance")


    TransactionDTO toDTO(Transaction transaction);
    List<TransactionDTO> toDTO(List<Transaction> transactionList);

    @Mapping(source = "debitAccountId", target = "transactionDebitAccount.id")
    @Mapping(source = "creditAccountId", target = "transactionCreditAccount.id")
    Transaction toTransaction(TransactionDTO transaction);
}
