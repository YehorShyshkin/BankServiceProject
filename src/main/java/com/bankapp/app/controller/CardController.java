package com.bankapp.app.controller;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CardDTO createCard(@RequestBody CardDTO cardDTO) {
        log.info("Create card: {}", cardDTO);
        return cardService.createCard(cardDTO);
    }

    @GetMapping("/find/{cardId}")
    @ResponseStatus(HttpStatus.OK)
    public CardDTO findCardById(@PathVariable UUID cardId) {
        log.info("Find card by id: {}", cardId);
        return cardService.findCardById(cardId);
    }

    @GetMapping("/update/{cardId}")
    @ResponseStatus(HttpStatus.OK)
    public CardDTO updateCard(@PathVariable UUID cardId,
                              @RequestBody CardDTO cardDTO) {
        log.info("Update card: {}", cardDTO);
        return cardService.updateCard(cardId, cardDTO);
    }


    @GetMapping("/delete/{cardId}")
    @ResponseStatus(HttpStatus.OK)
    public CardDTO deleteCard(@PathVariable UUID cardId) {
        log.info("Delete card: {}", cardId);
        return cardService.deleteCard(cardId);
    }
}
