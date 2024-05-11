package com.vadymkykalo.lms.service.user;

import com.vadymkykalo.lms.dto.UserDto;
import com.vadymkykalo.lms.dto.UserRegistrationDto;
import com.vadymkykalo.lms.dto.UserUpdateDto;
import com.vadymkykalo.lms.entity.Role;
import com.vadymkykalo.lms.entity.User;
import com.vadymkykalo.lms.exception.UserAlreadyExistException;
import com.vadymkykalo.lms.exception.ResourceNotFoundException;
import com.vadymkykalo.lms.repository.RoleRepository;
import com.vadymkykalo.lms.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isExistEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User register(UserRegistrationDto registrationDto) {
        if (isExistEmail(registrationDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + registrationDto.getEmail());
        }

        Role userRole = roleRepository.findByName(Role.RoleName.USER)
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
    public Optional<UserDto> get(Long id) {
        return userRepository.findById(id).map(this::mapToDto);
    }

    @Override
    public List<UserDto> get() {
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
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .isEnabled(user.isEnabled())
                .roles(user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet()))
                .build();
    }
}
