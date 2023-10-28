package com.bankapp.app.service.impl;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.entity.Card;
import com.bankapp.app.mapper.CardMapper;
import com.bankapp.app.repository.CardRepository;
import com.bankapp.app.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
        Optional<Card> cardOptional = cardRepository.findById(UUID.fromString(id));
        Card card = cardOptional.orElseThrow(() -> new NoSuchElementException("Card not found"));
        return cardMapper.toDTO(card);
    }
}
