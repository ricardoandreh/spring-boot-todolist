package br.com.rictodolist.todolist.domain.task;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_tasks")
public class TaskModel implements Serializable {
  @Serial
  private static final long serialVersionUID = 2L;

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(length = 50)
  private String title;

  private String description;
  private LocalDateTime startAt;
  private LocalDateTime endAt;
  private String priority;

  private UUID idUser;

  @CreationTimestamp
  private LocalDateTime createdAt;

  public void setTitle(String title) throws Exception {
    if (title.length() > 50) {
      throw new Exception("O campo title deve conter no máximo 50 caracteres");
    }

    this.title = title;
  }
}
