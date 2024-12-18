package br.com.rictodolist.todolist.services;

import br.com.rictodolist.todolist.dtos.jwt.AccessResponseDTO;
import br.com.rictodolist.todolist.dtos.jwt.LoginResponseDTO;
import br.com.rictodolist.todolist.dtos.jwt.RefreshRequestDTO;
import br.com.rictodolist.todolist.dtos.user.UserRequestDTO;
import br.com.rictodolist.todolist.dtos.user.UserResponseDTO;
import br.com.rictodolist.todolist.exceptions.UserAlreadyExistsException;
import br.com.rictodolist.todolist.exceptions.UserNotFoundException;
import br.com.rictodolist.todolist.infrastructure.enums.Role;
import br.com.rictodolist.todolist.mappers.UserMapper;
import br.com.rictodolist.todolist.models.UserModel;
import br.com.rictodolist.todolist.repositories.IUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    public UserResponseDTO register(UserRequestDTO userRequestDto) {
        this.userRepository
                .findByUsername(userRequestDto.username())
                .ifPresent((userDetails) -> {
                    throw new UserAlreadyExistsException(this.messageSource);
                });

        String encodedPassword = this.passwordEncoder.encode(userRequestDto.password());

        UserModel userModel = new UserModel();

        BeanUtils.copyProperties(userRequestDto, userModel);
        userModel.setPassword(encodedPassword);
        userModel.setRole(Role.USER);

        UserModel userCreated = this.userRepository.save(userModel);

        return this.userMapper.toDTO(userCreated);
    }

    public LoginResponseDTO login(UserRequestDTO userRequestDTO) {
        UsernamePasswordAuthenticationToken usernamePassword =
                new UsernamePasswordAuthenticationToken(userRequestDTO.username(), userRequestDTO.password());

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        UserModel user = (UserModel) auth.getPrincipal();

        String token = this.jwtService.generateAccessToken(user);
        String refreshToken = this.jwtService.generateRefreshToken(user);

        return new LoginResponseDTO(token, refreshToken);
    }

    public AccessResponseDTO refresh(RefreshRequestDTO refreshRequestDTO) {
        String refreshToken = refreshRequestDTO.refreshToken();

        String username = this.jwtService.validateToken(refreshToken);

        UserModel user = (UserModel) this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(this.messageSource, username));

        String newAccessToken = this.jwtService.generateAccessToken(user);

        return new AccessResponseDTO(newAccessToken);
    }
}
