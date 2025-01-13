package com.randre.task_tracker.repositories;

import com.randre.task_tracker.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserDetails> findByUsername(String username);
}
