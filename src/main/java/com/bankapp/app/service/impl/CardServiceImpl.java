package com.bankapp.app.service.impl;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.exception.CardNotFoundException;
import com.bankapp.app.generator.CardGenerator;
import com.bankapp.app.mapper.CardMapper;
import com.bankapp.app.model.Card;
import com.bankapp.app.model.enums.PaymentSystem;
import com.bankapp.app.repository.CardRepository;
import com.bankapp.app.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Override
    @Transactional
    public CardDTO findCardById(UUID cardId) {
        return cardMapper.toDto(getById(cardId));
    }

    @Override
    @Transactional
    public Card getById(UUID cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(
                        String.format("Card with id %s not found", cardId)));
    }


    @Override
    @Transactional
    public CardDTO createCard(CardDTO cardDTO) {
        Card newCard = cardMapper.toEntity(cardDTO);

        if (newCard.getNumber() == null
                || newCard.getNumber().isEmpty()) {
            newCard.setNumber(CardGenerator
                    .generateCardNumber(PaymentSystem.valueOf(cardDTO.getPaymentSystem())));
        }

        if (newCard.getCvv() == null) {
            newCard.setCvv(Integer
                    .valueOf(CardGenerator.generateCardCVV()));
        }

        if (newCard.getExpirationDate() == null) {
            newCard.setExpirationDate(CardGenerator
                    .generateCardExpirationDate());
        }

        return cardMapper.toDto(cardRepository.save(newCard));
    }
}

