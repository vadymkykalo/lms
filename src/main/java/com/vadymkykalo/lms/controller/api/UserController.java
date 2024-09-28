package com.vadymkykalo.lms.controller.api;

import com.vadymkykalo.lms.dto.ApiResponse;
import com.vadymkykalo.lms.dto.UserCreateDto;
import com.vadymkykalo.lms.dto.UserDto;
import com.vadymkykalo.lms.dto.UserUpdateDto;
import com.vadymkykalo.lms.exception.ResourceNotFoundException;
import com.vadymkykalo.lms.service.user.UserService;
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
    public ResponseEntity<?> getAll() {
        List<UserDto> users = userService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(ApiResponse.success(users, HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        UserDto user = userService.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return ResponseEntity.ok(ApiResponse.success(user, HttpStatus.OK));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody UserCreateDto userData) {
        UserDto createdUser = userService.mapToDto(userService.register(userData));
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/v1/users/" + createdUser.getId())
                .body(ApiResponse.success(createdUser, HttpStatus.CREATED));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDto userData) {
        UserDto updatedUser = userService.update(id, userData);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!userService.isExistUserById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
