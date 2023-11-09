package com.bankapp.app.controller;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Manager;
import com.bankapp.app.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    public ResponseEntity<String> createManager(@RequestBody Manager manager) {
        managerService.save(manager);
        return ResponseEntity.ok("Manager was create! Sucess!");
    }

    @PostMapping("/update_manager/{id}")
    public ResponseEntity<Manager> updateManager(@PathVariable UUID id, @RequestBody ManagerDTO managerDTO) {
        Manager updateManager = managerService.updateManager(id, managerDTO);
        if (updateManager != null) {
            return new ResponseEntity<>(updateManager, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("delete_manager/{id}")
    public ResponseEntity<String> deleteManager(@PathVariable UUID id) {
        boolean deleteManager = managerService.deleteManager(id);
        if (deleteManager) {
            return new ResponseEntity<>("Manager deleted successfully!", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Manager not found!", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/manager_client/")
    public ResponseEntity<String> mergeManagerAndClient(@RequestBody Map<String, UUID> request) {
        UUID managerId = request.get("managerId");
        UUID clientId = request.get("clientId");

        boolean merge = managerService.mergeManagerAndClient(clientId, managerId);

        if (merge) {
            return new ResponseEntity<>("Manager has been successfully added to the client!", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Manager has not been successfully added to the client!", HttpStatus.NOT_FOUND);
        }
    }
}
