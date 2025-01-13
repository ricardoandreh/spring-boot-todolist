package com.randre.task_tracker.mappers;

import com.randre.task_tracker.dtos.user.UserResponseDTO;
import com.randre.task_tracker.models.UserModel;
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
