package br.com.rictodolist.todolist.models;

import br.com.rictodolist.todolist.validations.annotations.FutureDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_tasks")
public class TaskModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @NotBlank
    @Size(max = 50, message = "O título deve conter no máximo 50 caracteres")
    private String title;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    @FutureDate
    private LocalDateTime startAt;

    @NotNull
    @FutureDate
    private LocalDateTime endAt;

    @NotNull
    @NotBlank
    private String priority;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserDetails user;
}
