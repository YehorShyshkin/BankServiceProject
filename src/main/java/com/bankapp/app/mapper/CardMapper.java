package com.bankapp.app.mapper;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(source = "cardNumber", target = "cardNumber")
//    @Mapping(source = "card.holderName", target = "holderName")
    @Mapping(source = "cardBalance", target = "cardBalance")
    @Mapping(source = "cardPaymentSystem", target = "cardPaymentSystem")
    @Mapping(source = "cardStatus", target = "cardStatus")
    @Mapping(source = "account.accountName", target = "accountName")
    @Mapping(source = "account.accountType", target = "accountType")
    @Mapping(source = "account.accountStatus", target = "accountStatus")
    @Mapping(source = "account.currencyCode", target = "currencyCode")
    @Mapping(source = "account.accountBalance", target = "accountBalance")
    @Mapping(source = "account.client.clientStatus", target = "clientStatus")
    @Mapping(source = "account.client.clientLastName", target = "clientLastName")

    CardDTO toDTO(Card card);

    List<CardDTO> toDTO(List<Card> cardList);
}
