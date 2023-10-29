package com.bankapp.app.controller;

import com.bankapp.app.dto.CardDTO;
import com.bankapp.app.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

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
}
