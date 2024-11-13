package br.com.rictodolist.todolist.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.rictodolist.todolist.domain.user.UserModel;
import br.com.rictodolist.todolist.exceptions.UserAlreadyExistException;
import br.com.rictodolist.todolist.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    public UserModel create(UserModel userModel) {
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if (user != null) {
            throw new UserAlreadyExistException();
        }

        String passwordHashed = BCrypt.withDefaults()
                .hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashed);

        return this.userRepository.save(userModel);
    }
}
