package com.bankapp.app.service.impl;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.dto.CardStatusUpdateDTO;
import com.bankapp.app.entity.Account;
import com.bankapp.app.entity.Card;
import com.bankapp.app.mapper.CardMapper;
import com.bankapp.app.repository.CardRepository;
import com.bankapp.app.service.CardService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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

    @Override
    public Card findCardByID(UUID cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found!"));
    }

    @Override
    public void save(Card card) {
        cardRepository.save(card);
    }

    @Override
    public ResponseEntity<String> deleteCard(UUID id) {
        Card currentCard = findCardByID(id);

        if (currentCard != null) {
            Account account = currentCard.getAccount();

            if (account != null) {
                BigDecimal balance = account.getBalance();

                if (balance.compareTo(BigDecimal.ZERO) == 0) {
                    cardRepository.deleteById(id);
                    return ResponseEntity.ok("Card deleted successfully!");
                } else {
                    return ResponseEntity.badRequest().body("Balance on the card must be zero!");
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public Card updateCard(UUID id, CardStatusUpdateDTO cardDTO) {
        Card currentCard = findCardByID(id);
        Card updatedCard = cardMapper.updateCardFromDTO(cardDTO, currentCard);
        return cardRepository.save(updatedCard);
    }
}

