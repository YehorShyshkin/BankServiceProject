package com.bankapp.app.controller;


import com.bankapp.app.dto.AgreementDTO;
import com.bankapp.app.entity.Agreement;
import com.bankapp.app.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agreements")
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementService agreementService;

    @GetMapping("/all")
    public List<AgreementDTO> findAll() {
        return agreementService.findAll();
    }

    @GetMapping("/{id}")
    public AgreementDTO getAgreementDTO(@PathVariable("id") String id) {
        String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        if (!id.matches(uuidPattern)) {
            throw new IllegalArgumentException("ID is not a valid UUID");
        }

        return agreementService.getAgreementDTO(id);
    }

    @PostMapping("/create_agreement")
    public ResponseEntity<String> createAgreement(@RequestBody Agreement agreement){
        agreementService.save(agreement);
        return ResponseEntity.ok("Agreement was create! Success! ");
    }
}
