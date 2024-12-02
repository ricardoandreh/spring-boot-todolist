package br.com.rictodolist.todolist.mappers;

import br.com.rictodolist.todolist.dtos.user.UserResponseDTO;
import br.com.rictodolist.todolist.models.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toDTO(UserModel model) {
        return new UserResponseDTO(
                model.getId(),
                model.getUsername(),
                model.getName(),
                model.getCreatedAt()
        );
    }
}
