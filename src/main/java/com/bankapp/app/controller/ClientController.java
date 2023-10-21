package com.bankapp.app.controller;


import com.bankapp.app.dto.ClientDTO;
import com.bankapp.app.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {


    private final ClientService clientService;

    @GetMapping("/all")
    public List<ClientDTO> findAll(){
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public ClientDTO getClientDTO(@PathVariable("id") String id){
        return clientService.getClientDTO(id);
    }

}
