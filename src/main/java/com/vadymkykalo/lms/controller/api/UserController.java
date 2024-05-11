package com.vadymkykalo.lms.controller.api;

import com.vadymkykalo.lms.dto.UserRegistrationDto;
import com.vadymkykalo.lms.dto.UserDto;
import com.vadymkykalo.lms.dto.UserUpdateDto;
import com.vadymkykalo.lms.exception.ResourceNotFoundException;
import com.vadymkykalo.lms.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.get();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(users);
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto user = userService.get(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserRegistrationDto userData) {
        return ResponseEntity.ok().body(userService.mapToDto(userService.register(userData)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserUpdateDto userData) {
        return ResponseEntity.ok().body(userService.update(id, userData));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void destroy(@PathVariable Long id) {
        userService.delete(id);
    }
}
