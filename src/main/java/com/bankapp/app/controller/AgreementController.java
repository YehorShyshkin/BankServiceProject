package com.bankapp.app.controller;


import com.bankapp.app.controller.dto.AgreementDTO;
import com.bankapp.app.entity.Agreement;
import com.bankapp.app.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public ResponseEntity<String> createAgreement(@RequestBody Agreement agreement) {
        agreementService.save(agreement);
        return ResponseEntity.ok("Agreement was create! Success! ");
    }


    @PostMapping("/update_agreement/{id}")
    public ResponseEntity<Agreement> updateAgreement(@PathVariable UUID id, @RequestBody AgreementDTO agreementDTO) {
        Agreement updateAgreement = agreementService.updateAgreement(id, agreementDTO);
        if (updateAgreement != null) {
            return new ResponseEntity<>(updateAgreement, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete_agreement/{id}")
    public ResponseEntity<String> deleteAgreement(@PathVariable UUID id) {
        boolean deleteAgreement = agreementService.deleteAgreement(id);
        if (deleteAgreement) {
            return new ResponseEntity<>("Agreement deleted successfully!", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Agreement not found!", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/agreement_product_account/")
    public ResponseEntity<String> mergeAgreementProductAndAccount(@RequestBody Map<String, UUID> request) {
        UUID agreementId = request.get("agreementId");
        UUID productId = request.get("productId");
        UUID accountId = request.get("accountId");

        boolean merge = agreementService.mergeAgreementProductAndAccount(accountId, productId, agreementId);
        if (merge) {
            return new ResponseEntity<>("Agreement has been successfully added to the product and account!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Agreement has not been successfully added to the product and account!", HttpStatus.BAD_REQUEST);
        }
    }
}
