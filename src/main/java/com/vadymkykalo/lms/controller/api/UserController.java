package com.vadymkykalo.lms.controller.api;

import com.vadymkykalo.lms.dto.UserCreateDto;
import com.vadymkykalo.lms.dto.UserDto;
import com.vadymkykalo.lms.dto.UserUpdateDto;
import com.vadymkykalo.lms.entity.User;
import com.vadymkykalo.lms.exception.ResourceNotFoundException;
import com.vadymkykalo.lms.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    ResponseEntity<List<UserDto>> index() {
        List<User> users = repository.findAll();
        List<UserDto> result = users.stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .isEnabled(user.isEnabled())
                        .build())
                .toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(result);
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserDto show(@PathVariable Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .isEnabled(user.isEnabled())
                .build();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    UserDto create(@Valid @RequestBody UserCreateDto userData) {
        User user = User.builder()
                .lastName(userData.getLastName())
                .firstName(userData.getFirstName())
                .email(userData.getEmail())
                .passwordDigest(passwordEncoder.encode(userData.getPassword()))
                .isEnabled(true)
                .build();

        User updateUser = repository.saveAndFlush(user);

        return UserDto.builder()
                .id(updateUser.getId())
                .username(updateUser.getUsername())
                .password(updateUser.getPassword())
                .firstName(updateUser.getFirstName())
                .lastName(updateUser.getLastName())
                .isEnabled(updateUser.isEnabled())
                .build();
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserDto update(@RequestBody UserUpdateDto userData, @PathVariable Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

        user.setFirstName(userData.getFirstName());

        User updateUser = repository.saveAndFlush(user);

        return UserDto.builder()
                .id(updateUser.getId())
                .username(updateUser.getUsername())
                .password(updateUser.getPassword())
                .firstName(updateUser.getFirstName())
                .lastName(updateUser.getLastName())
                .isEnabled(updateUser.isEnabled())
                .build();
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void destroy(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
