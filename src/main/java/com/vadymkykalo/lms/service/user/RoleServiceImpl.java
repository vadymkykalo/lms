package com.vadymkykalo.lms.service.user;

import com.vadymkykalo.lms.constants.CacheConstants;
import com.vadymkykalo.lms.entity.Role;
import com.vadymkykalo.lms.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    @Cacheable(value = CacheConstants.ROLES_CACHE, key = "#name.toString()")
    public Optional<Role> getRole(Role.RoleName name) {
        log.info("Fetching from database for name: {}", name);
        return roleRepository.findByName(name);
    }
}
