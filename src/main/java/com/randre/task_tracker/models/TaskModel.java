package com.randre.task_tracker.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.randre.task_tracker.constants.ErrorMessages;
import com.randre.task_tracker.infrastructure.enums.Priority;
import com.randre.task_tracker.validations.annotations.DateRange;
import com.randre.task_tracker.validations.annotations.FutureDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_tasks", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id")
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DateRange(
        startDateField = "startAt",
        endDateField = "endAt"
)
public class TaskModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
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

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Priority priority;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private UserModel user;

    @Override
    public String toString() {
        return "Task{" +
                "title='" + this.title + '\'' +
                ", description='" + this.description + '\'' +
                ", startAt=" + this.startAt +
                ", endAt=" + this.endAt +
                ", priority=" + this.priority +
                '}';
    }
}
