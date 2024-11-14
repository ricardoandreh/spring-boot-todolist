package br.com.rictodolist.todolist.repositories;

import br.com.rictodolist.todolist.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByUsername(String username);

}
