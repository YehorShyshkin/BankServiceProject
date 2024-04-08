package com.bankapp.app.service;

import com.bankapp.app.controller.dto.CardDTO;
import com.bankapp.app.controller.dto.CardStatusUpdateDTO;
import com.bankapp.app.entity.Card;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CardService {

    List<CardDTO> findAll();

    CardDTO getCardDTO(String id);

    Card findCardByID(UUID cardId);

    void save(Card card);

    ResponseEntity<String> deleteCard(UUID id);

    Card updateCard(UUID id, CardStatusUpdateDTO cardDTO);
}
