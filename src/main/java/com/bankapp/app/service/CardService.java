package com.bankapp.app.service;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.entity.Card;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CardService {

    List<CardDTO> findAll();

    CardDTO getCardDTO(String id);

    void save(Card card);

    ResponseEntity<String> deleteCard(UUID id);
}
