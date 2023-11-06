package com.bankapp.app.controller;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Manager;
import com.bankapp.app.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/managers")
@RequiredArgsConstructor

public class ManagerController {
    private final ManagerService managerService;

    @GetMapping("/all")
    public List<ManagerDTO> findAll() {
        return managerService.findAll();
    }

    @GetMapping("/{id}")
    public ManagerDTO getManagerDTO(@PathVariable("id") String id) {
        String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        if (!id.matches(uuidPattern)) {
            throw new IllegalArgumentException("ID is not a valid UUID");
        }
        return managerService.getManagerDTO(id);
    }

    @PostMapping("/create_managers")
    public ResponseEntity<String> createManager (@RequestBody Manager manager){
        managerService.save(manager);
        return ResponseEntity.ok("Manager was create! Sucess!");
    }


}
