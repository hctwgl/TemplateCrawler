package com.seveniu.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserssRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
