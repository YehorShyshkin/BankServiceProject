package com.bankapp.app.mapper;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.dto.CardStatusUpdateDTO;
import com.bankapp.app.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CardMapper {
    CardDTO toDTO(Card card);
    List<CardDTO> toDTO(List<Card> cardList);

    Card updateCardFromDTO(CardStatusUpdateDTO cardStatusUpdateDTO, @MappingTarget Card card);
}
