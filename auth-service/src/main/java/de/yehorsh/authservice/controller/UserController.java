package de.yehorsh.authservice.controller;

import de.yehorsh.authservice.dto.UserDto;
import de.yehorsh.authservice.model.entity.User;
import de.yehorsh.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDto userDto) {
        User createdUser = userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser.getUserId().toString());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto findById(@PathVariable("id") String id) {
        return userService.findById(id);
    }
}
