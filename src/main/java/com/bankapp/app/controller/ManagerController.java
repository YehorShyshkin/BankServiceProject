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

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/managers")
@RequiredArgsConstructor

public class ManagerController {
    private final ManagerService managerService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ManagerDTO> findAll() {
        return managerService.findAll();
    }

    @RequestMapping(value = "/find/{id}", method = GET)
    @ResponseStatus(HttpStatus.OK)
    public ManagerDTO findManagerDTOById(@PathVariable("id") UUID managerId){
        return managerService.findManagerDTOById(managerId);
    }

    @RequestMapping(value = "/create_managers/", method = POST)
    @ResponseStatus(HttpStatus.OK)
    public ManagerDTO createManager(@RequestBody Manager manager) {
        return managerService.createManager(manager);
    }

    @RequestMapping(value = "/update_manager/{id}", method = POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Manager> updateManager(@PathVariable UUID id, @RequestBody ManagerDTO managerDTO) {
        Manager updateManager = managerService.updateManager(id, managerDTO);
        if (updateManager != null) {
            return new ResponseEntity<>(updateManager, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/delete_manager/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteManager(@PathVariable UUID id) {
        boolean deleteManager = managerService.deleteManager(id);
        if (deleteManager) {
            return new ResponseEntity<>("Manager deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Manager not found!", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/manager_client/", method = POST)
    @ResponseStatus(HttpStatus.OK)
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
