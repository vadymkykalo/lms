package com.vadymkykalo.lms.service.user;

import com.vadymkykalo.lms.constants.CacheConstants;
import com.vadymkykalo.lms.entity.Role;
import com.vadymkykalo.lms.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Cacheable(value = CacheConstants.ROLES_CACHE, key = "#name.toString()")
    public Optional<Role> getRole(Role.RoleName name) {
        return roleRepository.findByName(name);
    }
}
