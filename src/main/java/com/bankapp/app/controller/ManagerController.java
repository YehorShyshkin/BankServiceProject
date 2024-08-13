package com.bankapp.app.controller;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping(value = "/creates")
    @ResponseStatus(HttpStatus.CREATED)
    public ManagerDTO createManager(@RequestBody @Valid ManagerDTO managerDTO) {
        log.info("Create manager: {}", managerDTO);
        return managerService.createManager(managerDTO);
    }

    @GetMapping(value = "/find/{managerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ManagerDTO> findManagerById(@PathVariable UUID managerId){
        ManagerDTO managerDTO = managerService.findManagerById(managerId);
        log.info("Find manager by id: {}", managerId);
        return ResponseEntity.ok(managerDTO);
    }
}
