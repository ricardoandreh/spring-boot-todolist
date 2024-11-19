package br.com.rictodolist.todolist.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.rictodolist.todolist.dtos.UserRequestDTO;
import br.com.rictodolist.todolist.dtos.UserResponseDTO;
import br.com.rictodolist.todolist.exceptions.UserAlreadyExistException;
import br.com.rictodolist.todolist.models.UserModel;
import br.com.rictodolist.todolist.repositories.IUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    public UserModel create(UserRequestDTO userRequestDto) {
        UserModel user = this.userRepository.findByUsername(userRequestDto.username());

        if (user != null) {
            throw new UserAlreadyExistException();
        }

        String passwordHashed = BCrypt.withDefaults()
                .hashToString(12, userRequestDto.password().toCharArray());

        UserModel userModel = new UserModel();

        BeanUtils.copyProperties(userRequestDto, userModel);
        userModel.setPassword(passwordHashed);

        return this.userRepository.save(userModel);
    }
}
