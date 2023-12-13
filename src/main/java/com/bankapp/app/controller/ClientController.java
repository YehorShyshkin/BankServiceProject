package com.bankapp.app.controller;


import com.bankapp.app.dto.ClientDTO;
import com.bankapp.app.entity.Client;
import com.bankapp.app.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ClientDTO> findAll() {
        return clientService.findAll();
    }

    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO getClientDTO(@PathVariable("id") String id) {
        String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        if (!id.matches(uuidPattern)) {
            throw new IllegalArgumentException("ID is not a valid UUID");
        }
        return clientService.getClientDTO(id);
    }

    @RequestMapping(value = "/create_clients/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO createClientDTO(@RequestBody Client client) {
        return clientService.createClientDTO(client);
    }
}
