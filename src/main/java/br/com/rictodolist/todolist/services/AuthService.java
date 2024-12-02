package br.com.rictodolist.todolist.services;

import br.com.rictodolist.todolist.config.security.Role;
import br.com.rictodolist.todolist.dtos.user.LoginResponseDTO;
import br.com.rictodolist.todolist.dtos.user.UserRequestDTO;
import br.com.rictodolist.todolist.dtos.user.UserResponseDTO;
import br.com.rictodolist.todolist.exceptions.UserAlreadyExistException;
import br.com.rictodolist.todolist.mappers.UserMapper;
import br.com.rictodolist.todolist.models.UserModel;
import br.com.rictodolist.todolist.repositories.IUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private UserMapper userMapper;

    public UserResponseDTO register(UserRequestDTO userRequestDto) {
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

        UserModel userCreated = this.userRepository.save(userModel);

        return this.userMapper.toDTO(userCreated);
    }

    public LoginResponseDTO login(UserRequestDTO userRequestDTO) {
        UsernamePasswordAuthenticationToken usernamePassword =
                new UsernamePasswordAuthenticationToken(userRequestDTO.username(), userRequestDTO.password());

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        String token = this.jwtService.generateToken((UserModel) auth.getPrincipal());

        return new LoginResponseDTO(token);
    }
}
