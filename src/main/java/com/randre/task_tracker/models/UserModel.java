package com.randre.task_tracker.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.randre.task_tracker.utils.ErrorMessages;
import com.randre.task_tracker.utils.SecurityConstants;
import com.randre.task_tracker.infrastructure.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_users", indexes = {
        @Index(name = "idx_user_username", columnList = "username")
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @Size(max = 30, message = ErrorMessages.MAXIMUM_NAME_SIZE)
    private String name;

    @NotBlank
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Enumerated(EnumType.ORDINAL)
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
