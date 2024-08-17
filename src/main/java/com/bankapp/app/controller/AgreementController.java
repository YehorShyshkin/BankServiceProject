package com.bankapp.app.controller;


import com.bankapp.app.dto.AgreementDTO;
import com.bankapp.app.service.AgreementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/agreements")
public class AgreementController {

    private final AgreementService agreementService;

    @PostMapping("/creates")
    @ResponseStatus(HttpStatus.CREATED)
    public AgreementDTO createAgreement(@RequestBody AgreementDTO agreementDTO) {
        log.info("Creating agreement {}", agreementDTO);
        return agreementService.createAgreement(agreementDTO);
    }

    @GetMapping("/find/{agreementId}")
    @ResponseStatus(HttpStatus.OK)
    public AgreementDTO findAgreementById(@PathVariable UUID agreementId) {
        log.info("Finding agreement {}", agreementId);
        return agreementService.findAgreementById(agreementId);
    }

    @GetMapping("/update/{agreementId}")
    @ResponseStatus(HttpStatus.OK)
    public AgreementDTO updateAgreement
            (@PathVariable UUID agreementId,
             @RequestBody AgreementDTO agreementDTO) {
        log.info("Updating agreement {}", agreementId);
        return agreementService.updateAgreement(agreementId, agreementDTO);
    }

    @GetMapping("/delete/{agreementId}")
    @ResponseStatus(HttpStatus.OK)
    public AgreementDTO softDeleteAgreement(@PathVariable UUID agreementId) {
        log.info("Deleting agreement {}", agreementId);
        return agreementService.softDeleteAgreement(agreementId);
    }
}
