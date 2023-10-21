package com.bankapp.app.controller;


import com.bankapp.app.dto.AgreementDTO;
import com.bankapp.app.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agreements")
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementService agreementService;
    @GetMapping("/all")
    public List<AgreementDTO> findAll(){
        return agreementService.findAll();
    }
    @GetMapping("/{id}")
    public AgreementDTO getAgreementDTO (@PathVariable("id") String id){
        return agreementService.getAgreementDTO(id);
    }
}
