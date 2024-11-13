package br.com.rictodolist.todolist.repositories;

import java.util.UUID;

import br.com.rictodolist.todolist.domain.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {
  UserModel findByUsername(String username);

}
