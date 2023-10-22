package com.bankapp.app.service.impl;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.mapper.CardMapper;
import com.bankapp.app.repository.CardRepository;
import com.bankapp.app.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Override
    public List<CardDTO> findAll() {
        return cardMapper.toDTO(cardRepository.findAll());
    }

    @Override
    public CardDTO getCardDTO(String id) {
        return cardMapper.toDTO(cardRepository.findById(UUID.fromString(id)).orElseThrow());
    }
}
