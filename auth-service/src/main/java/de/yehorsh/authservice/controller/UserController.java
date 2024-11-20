package de.yehorsh.authservice.controller;

import de.yehorsh.authservice.dto.UserDro;
import de.yehorsh.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/main_admin")
    @ResponseStatus(HttpStatus.CREATED)
    public UUID create(@Valid @RequestBody UserDro userDro){
        return userService.registerUser(userDro);
    }

    @GetMapping("/{id}")
    public UserDro findById(@PathVariable("id") String id){
        return userService.findById(id);
    }

}
