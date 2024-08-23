package com.bankapp.app.controller;


import com.bankapp.app.dto.ClientDTO;
import com.bankapp.app.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    // FIXME here and in all other controllers: why creates? create is better
    @PostMapping(value = "/creates")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTO createClient
            (@RequestBody @Valid ClientDTO clientDTO) {
        log.info("Create client {}", clientDTO);
        return clientService.createClient(clientDTO);
    }

    @GetMapping(value = "/find/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO findClientById(@PathVariable UUID clientId) {
        log.info("Find client by id: {}", clientId);
        return clientService.findClientById(clientId);
    }

    @GetMapping("/update/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO updateClient(@PathVariable UUID clientId,
                                  @RequestBody @Valid ClientDTO clientDTO) {
        log.info("Update client {}", clientDTO);
        return clientService.updateClient(clientId, clientDTO);
    }

    @GetMapping("/delete/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO softDeleteClient(@PathVariable UUID clientId) {
        log.info("Delete client {}", clientId);
        return clientService.softDeleteClient(clientId);
    }
}
