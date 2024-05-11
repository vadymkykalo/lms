package com.vadymkykalo.lms.service.user;

import com.vadymkykalo.lms.entity.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> getRole(Role.RoleName name);
}
