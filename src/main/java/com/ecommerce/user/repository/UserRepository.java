package com.ecommerce.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.user.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query methods (optional)

    // Example: Find a user by email
    Optional<User> findByEmail(String email);

    // Example: Check if a user exists by email
    boolean existsByEmail(String email);
}
