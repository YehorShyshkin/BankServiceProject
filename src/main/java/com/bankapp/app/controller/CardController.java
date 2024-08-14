package com.bankapp.app.controller;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.dto.CardStatusUpdateDTO;
import com.bankapp.app.model.Account;
import com.bankapp.app.model.Card;
import com.bankapp.app.model.enums.PaymentSystem;
import com.bankapp.app.generator.CardGenerator;
import com.bankapp.app.service.AccountService;
import com.bankapp.app.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final AccountService accountService;

    @GetMapping("/all")
    private List<CardDTO> findAll() {
        return cardService.findAll();
    }

    @GetMapping("/{id}")
    private CardDTO getCardDTO(@PathVariable("id") String id) {
        String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        if (!id.matches(uuidPattern)) {
            throw new IllegalArgumentException("ID is not a valid UUID");
        }
        return cardService.getCardDTO(id);
    }

    @PostMapping("/create_card/{account_id}")
    public ResponseEntity<String> createCard(@RequestBody Card card, @PathVariable("account_id") UUID id) {
        Account account = accountService.getAccountById(String.valueOf(id));
        if (account != null) {
            String generatedCardNumber = CardGenerator.generateCardNumber(PaymentSystem.VISA);
            card.setNumber(generatedCardNumber);
            card.setAccount(account);
            card.setExpirationDate(CardGenerator.generateCardExpirationDate());
            cardService.save(card);
            return ResponseEntity.ok("Card was created and linked to the account! Success!");
        } else {
            return ResponseEntity.badRequest().body("Account not found!");
        }
    }

    @DeleteMapping("/delete_card/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable UUID id) {
        return cardService.deleteCard(id);
    }

    @PostMapping("/update_card/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable UUID id, @RequestBody CardStatusUpdateDTO cardStatusUpdateDTO) {
        Card updateCard = cardService.updateCard(id, cardStatusUpdateDTO);
        return new ResponseEntity<>(updateCard, HttpStatus.ACCEPTED);
    }


}
