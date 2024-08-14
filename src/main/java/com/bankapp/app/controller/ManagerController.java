package com.bankapp.app.controller;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ManagerDTO createManager
            (@RequestBody @Valid ManagerDTO managerDTO) {
        log.info("Create manager: {}", managerDTO);
        return managerService.createManager(managerDTO);
    }

    @GetMapping(value = "/find/{managerId}")
    @ResponseStatus(HttpStatus.OK)
    public ManagerDTO findManagerById
            (@PathVariable UUID managerId) {
        log.info("Find manager by id: {}", managerId);
        return managerService.findManagerById(managerId);
    }
    @GetMapping(value = "update/{managerId}")
    @ResponseStatus(HttpStatus.OK)
    public ManagerDTO updateManager(@PathVariable UUID managerId,
                                    @RequestBody @Valid ManagerDTO managerDTO) {
        log.info("Update manager: {}", managerDTO);
        return managerService.updateManager(managerId,managerDTO);
    }
    @GetMapping("/delete/{managerId}")
    @ResponseStatus(HttpStatus.OK)
    public ManagerDTO deleteManager(@PathVariable UUID managerId) {
        log.info("Delete manager: {}", managerId);
        return managerService.deleteManager(managerId);
    }

}
