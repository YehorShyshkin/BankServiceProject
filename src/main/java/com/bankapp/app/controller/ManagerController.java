package com.bankapp.app.controller;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Manager;
import com.bankapp.app.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    @PostMapping("/update_manager/{id}")
    public ResponseEntity<Manager> updateManager(@PathVariable UUID id, @RequestBody ManagerDTO managerDTO){
        Manager updateManager = managerService.updateManager(id, managerDTO);
        if (updateManager!=null){
            return new ResponseEntity<>(updateManager, HttpStatus.ACCEPTED);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
