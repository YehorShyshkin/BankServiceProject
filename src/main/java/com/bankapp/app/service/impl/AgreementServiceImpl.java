package com.bankapp.app.service.impl;

import com.bankapp.app.entity.Agreement;
import com.bankapp.app.repository.AgreementRepository;
import com.bankapp.app.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository agreementRepository;

}
