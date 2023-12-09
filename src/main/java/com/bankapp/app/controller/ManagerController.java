package com.bankapp.app.controller;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Manager;
import com.bankapp.app.service.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/managers")
@RequiredArgsConstructor

public class ManagerController {
    private final ManagerService managerService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
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

    @RequestMapping(value = "/create_managers", method = POST)
    public ResponseEntity<Void> createManager(@RequestBody Manager manager, HttpServletRequest httpServletRequest) {
        if (manager == null) {
            return ResponseEntity.badRequest().build();
        }
        managerService.save(manager);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/update_manager/{id}", method = POST)
    public ResponseEntity<Manager> updateManager(@PathVariable UUID id, @RequestBody ManagerDTO managerDTO) {
        Manager updateManager = managerService.updateManager(id, managerDTO);
        if (updateManager != null) {
            return new ResponseEntity<>(updateManager, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/delete_manager/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteManager(@PathVariable UUID id) {
        boolean deleteManager = managerService.deleteManager(id);
        if (deleteManager) {
            return new ResponseEntity<>("Manager deleted successfully!", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Manager not found!", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/manager_client/", method = POST)
    public ResponseEntity<String> mergeManagerAndClient(@RequestBody Map<String, UUID> request) {
        UUID managerId = request.get("managerId");
        UUID clientId = request.get("clientId");

        boolean merge = managerService.mergeManagerAndClient(clientId, managerId);

        if (merge) {
            return new ResponseEntity<>("Manager has been successfully added to the client!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Manager has not been successfully added to the client!", HttpStatus.BAD_REQUEST);
        }
    }
}
