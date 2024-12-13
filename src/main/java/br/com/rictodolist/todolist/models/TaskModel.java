package br.com.rictodolist.todolist.models;

import br.com.rictodolist.todolist.constants.ErrorMessages;
import br.com.rictodolist.todolist.validations.annotations.DateRange;
import br.com.rictodolist.todolist.validations.annotations.FutureDate;
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

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_tasks")
@DateRange(
        startDateField = "startAt",
        endDateField = "endAt"
)
public class TaskModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(max = 50, message = ErrorMessages.MAXIMUM_TITLE_SIZE)
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @FutureDate
    private LocalDateTime startAt;

    @NotNull
    @FutureDate
    private LocalDateTime endAt;

    @NotBlank
    private String priority;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotNull
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private UserModel user;
}
