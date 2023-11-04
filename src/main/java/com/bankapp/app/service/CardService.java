package com.bankapp.app.service;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.entity.Card;

import java.util.List;

public interface CardService {

    List<CardDTO> findAll();
    CardDTO getCardDTO(String id);

    void save(Card card);

}
