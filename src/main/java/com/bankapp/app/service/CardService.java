package com.bankapp.app.service;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.model.Card;

import java.util.UUID;

public interface CardService {

    CardDTO createCard(CardDTO cardDTO);

    CardDTO findCardById(UUID cardId);
    Card getById(UUID cardId);

    CardDTO updateCard(UUID cardId, CardDTO cardDTO);

    CardDTO deleteCard(UUID cardId);

}
