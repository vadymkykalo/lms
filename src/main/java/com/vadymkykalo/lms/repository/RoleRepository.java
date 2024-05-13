package com.vadymkykalo.lms.repository;

import com.vadymkykalo.lms.constants.CacheConstants;
import com.vadymkykalo.lms.entity.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(Role.RoleName name);

    // todo move cache into Role service
    @CacheEvict(value = CacheConstants.ROLES_CACHE, allEntries = true)
    @Override
    <S extends Role> @NotNull S save(@NotNull S entity);

    @CacheEvict(value = CacheConstants.ROLES_CACHE, allEntries = true)
    @Override
    void delete(@NotNull Role role);
}
