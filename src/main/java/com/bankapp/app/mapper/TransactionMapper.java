package com.bankapp.app.mapper;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "transactionType", target = "transactionType")
    @Mapping(source = "transactionAmount", target = "transactionAmount")
    @Mapping(source = "transactionDescription", target = "transactionDescription")
    @Mapping(source = "transactionCreatedAt", target = "transactionCreatedAt")

    @Mapping(source = "transactionDebitAccount.accountName", target = "debitAccountName")
    @Mapping(source = "transactionDebitAccount.accountType", target = "debitAccountType")
    @Mapping(source = "transactionDebitAccount.accountStatus", target = "debitAccountStatus")
    @Mapping(source = "transactionDebitAccount.currencyCode", target = "debitAccountCurrencyCode")
    @Mapping(source = "transactionDebitAccount.accountBalance", target = "debitAccountBalance")
    @Mapping(source = "transactionDebitAccount.client.clientStatus", target = "debitClientStatus")
    @Mapping(source = "transactionDebitAccount.client.clientLastName", target = "debitClientLastName")

    @Mapping(source = "transactionCreditAccount.accountName", target = "creditAccountName")
    @Mapping(source = "transactionCreditAccount.accountType", target = "creditAccountType")
    @Mapping(source = "transactionCreditAccount.accountStatus", target = "creditAccountStatus")
    @Mapping(source = "transactionCreditAccount.currencyCode", target = "creditAccountCurrencyCode")
    @Mapping(source = "transactionCreditAccount.accountBalance", target = "creditAccountBalance")
    @Mapping(source = "transactionCreditAccount.client.clientStatus", target = "creditClientStatus")
    @Mapping(source = "transactionCreditAccount.client.clientLastName", target = "creditClientLastName")

    TransactionDTO toDTO(Transaction transaction);
    List<TransactionDTO> toDTO(List<Transaction> transactionList);

}
