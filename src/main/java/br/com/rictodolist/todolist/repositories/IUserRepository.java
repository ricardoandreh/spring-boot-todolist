package br.com.rictodolist.todolist.repositories;

import br.com.rictodolist.todolist.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserDetails> findByUsername(String username);
}
