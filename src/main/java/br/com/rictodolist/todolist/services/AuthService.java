package br.com.rictodolist.todolist.services;

import br.com.rictodolist.todolist.config.security.Role;
import br.com.rictodolist.todolist.dtos.UserRequestDTO;
import br.com.rictodolist.todolist.exceptions.UserAlreadyExistException;
import br.com.rictodolist.todolist.models.UserModel;
import br.com.rictodolist.todolist.repositories.IUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserModel register(UserRequestDTO userRequestDto) {
        this.userRepository
                .findByUsername(userRequestDto.username())
                .ifPresent((userDetails) -> {
                    throw new UserAlreadyExistException();
                });

        String passwordHashed = new BCryptPasswordEncoder().encode(userRequestDto.password());

        UserModel userModel = new UserModel();

        BeanUtils.copyProperties(userRequestDto, userModel);
        userModel.setPassword(passwordHashed);
        userModel.setRole(Role.USER);

        return this.userRepository.save(userModel);
    }

    public String login(UserRequestDTO userRequestDTO) {
        UsernamePasswordAuthenticationToken usernamePassword =
                new UsernamePasswordAuthenticationToken(userRequestDTO.username(), userRequestDTO.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        return this.jwtService.generateToken((UserModel) auth.getPrincipal());
    }
}
