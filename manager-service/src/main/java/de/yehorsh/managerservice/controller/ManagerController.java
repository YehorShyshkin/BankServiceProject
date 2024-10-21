package de.yehorsh.managerservice.controller;

import de.yehorsh.managerservice.dto.ManagerCreateDto;
import de.yehorsh.managerservice.dto.ManagerDto;
import de.yehorsh.managerservice.dto.ManagerUpdateDto;
import de.yehorsh.managerservice.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/managers")
public class ManagerController {
    private final ManagerService managerService;

    @PostMapping
    public ResponseEntity<String> createManager(@RequestBody @Valid ManagerCreateDto managerCreateDto) {
        managerService.createNewManager(managerCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Manager was successfully created!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerDto> findManager(@PathVariable("id") UUID id) {
        ManagerDto managerDto = ManagerDto.fromManager(managerService.findManagerById(id));
        return ResponseEntity.status(HttpStatus.OK).body(managerDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateManager(@PathVariable("id") UUID id,
                                                @RequestBody @Valid ManagerUpdateDto managerUpdateDto) {
        managerService.updateManager(id, managerUpdateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteManager(@PathVariable("id") UUID id) {
        managerService.deleteManager(id);
        return ResponseEntity.status(HttpStatus.OK).body("Manager with ID " + id + " was deleted!");
    }
}
