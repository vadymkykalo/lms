package com.vadymkykalo.lms.service.user;

import com.vadymkykalo.lms.dto.UserDto;
import com.vadymkykalo.lms.dto.UserCreateDto;
import com.vadymkykalo.lms.dto.UserUpdateDto;
import com.vadymkykalo.lms.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDto mapToDto(User user);

    User register(UserCreateDto registrationDto);

    Optional<UserDto> get(Long id);

    List<UserDto> get();

    UserDto update(Long id, UserUpdateDto userDto);

    boolean isExistEmail(String email);

    void delete(Long id);
}
