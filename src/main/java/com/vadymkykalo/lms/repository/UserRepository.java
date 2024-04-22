package com.vadymkykalo.lms.repository;

import com.vadymkykalo.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}