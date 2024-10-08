package com.vadymkykalo.lms.service.user;

import com.vadymkykalo.lms.dto.UserDto;
import com.vadymkykalo.lms.dto.UserCreateDto;
import com.vadymkykalo.lms.dto.UserUpdateDto;
import com.vadymkykalo.lms.entity.Role;
import com.vadymkykalo.lms.entity.User;
import com.vadymkykalo.lms.exception.UserAlreadyExistException;
import com.vadymkykalo.lms.exception.ResourceNotFoundException;
import com.vadymkykalo.lms.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isExistEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean isExistUserById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User register(UserCreateDto registrationDto) {
        if (isExistEmail(registrationDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + registrationDto.getEmail());
        }

        Role userRole = roleService.getRole(Role.RoleName.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

        return userRepository.saveAndFlush(User.builder()
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .email(registrationDto.getEmail())
                .passwordDigest(passwordEncoder.encode(registrationDto.getPassword()))
                .roles(Collections.singleton(userRole))
                .isEnabled(true)
                .build());
    }

    @Override
    public Optional<UserDto> getById(Long id) {
        return userRepository.findById(id).map(this::mapToDto);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto update(Long id, UserUpdateDto userDto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());

            user = userRepository.saveAndFlush(user);

            return mapToDto(user);
        } else {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }

    @Override
    public UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .isEnabled(user.isEnabled())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .build();
    }
}
