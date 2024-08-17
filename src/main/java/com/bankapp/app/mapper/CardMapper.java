package com.bankapp.app.mapper;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy =
                NullValuePropertyMappingStrategy.IGNORE)
public interface CardMapper {

    @Mapping(source = "clientId", target = "client.id")
    @Mapping(source = "accountId", target = "account.id")
    Card toEntity(CardDTO cardDTO);

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "account.id", target = "accountId")
    CardDTO toDto(Card card);

    @Mapping(target = "client.id", source = "clientId")
    @Mapping(target = "account.id", source = "accountId")
    void updateCardFromDTO(CardDTO cardDTO, @MappingTarget Card card);
}
