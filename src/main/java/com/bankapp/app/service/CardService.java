package com.bankapp.app.service;

import com.bankapp.app.dto.CardDTO;

import java.util.List;

public interface CardService {

    List<CardDTO> findAll();
    CardDTO getCardDTO(String id);
}
