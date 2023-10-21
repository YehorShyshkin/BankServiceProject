package com.bankapp.app.controller;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/managers")
@RequiredArgsConstructor

public class ManagerController {
    private final ManagerService managerService;

    @GetMapping("/all")
    public List<ManagerDTO> findAll(){
        return managerService.findAll();
    }
    @GetMapping("/{id}")
    public ManagerDTO getManagerDTO(@PathVariable("id") String id){
        return managerService.getManagerDTO(id);
    }
}
