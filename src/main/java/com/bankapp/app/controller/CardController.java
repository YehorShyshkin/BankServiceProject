package com.bankapp.app.controller;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.entity.Account;
import com.bankapp.app.entity.Card;
import com.bankapp.app.entity.Client;
import com.bankapp.app.enums.PaymentSystem;
import com.bankapp.app.service.CardService;
import com.bankapp.app.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final ClientService clientService;

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

    @PostMapping("/create_card/{client_id}")
    public ResponseEntity<String> createCard(@RequestBody Card card, @PathVariable("client_id") UUID id) {
        Client client = clientService.getClient(id);
        if (client != null) {
            String generatedCardNumber = card.generateCardNumber(PaymentSystem.VISA);
            card.setCardNumber(generatedCardNumber);

            List<Account> clientAccounts = client.getAccounts();
            if (!clientAccounts.isEmpty()) {
                // Выберите счет, который хотите связать с картой (например, первый счет)
                Account clientAccount = clientAccounts.get(0);

                // Установите этот счет в карту
                card.setAccount(clientAccount);

                // Задайте срок действия карты
                card.setExpirationDate(LocalDate.of(2025, 12, 31));

                cardService.save(card);
                return ResponseEntity.ok("Card was created! Success!");
            } else {
                return ResponseEntity.badRequest().body("Client has no accounts to link to the card.");
            }
        } else {
            return ResponseEntity.badRequest().body("Client was not found!");
        }




//        Client client = clientService.getClient(id);
//        if (client != null) {
//            String generatedCardNumber = card.generateCardNumber(PaymentSystem.VISA);
//            card.setCardNumber(generatedCardNumber);
//            Account clientAccount = client.getAccount();
//
//            card.setExpirationDate(LocalDate.of(2025, 12, 31));
//            cardService.save(card);
//            return ResponseEntity.ok("Card was create! Success!");
//        } else {
//            return ResponseEntity.badRequest().body("Card was not found!");
//        }
    }
}
