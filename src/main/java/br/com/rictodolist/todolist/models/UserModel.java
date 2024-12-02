package br.com.rictodolist.todolist.models;

import br.com.rictodolist.todolist.constants.SecurityConstants;
import br.com.rictodolist.todolist.infrastructure.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_users")
public class UserModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    @NotNull
    @NotBlank
    private String username;

    @Size(max = 30, message = "O nome deve possuir no máximo 30 caracteres")
    private String name;

    @NotNull
    @NotBlank
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<TaskModel> tasks;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = this.role.getPermissions().stream().map(
                permissionEnum -> new SimpleGrantedAuthority(permissionEnum.name())
        ).collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority(SecurityConstants.ROLE_PREFIX + this.role.name()));

        return authorities;
    }
}
