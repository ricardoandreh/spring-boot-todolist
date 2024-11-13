package br.com.rictodolist.todolist.domain.user;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_users")
public class UserModel implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(unique = true)
  public String username;
  public String name;
  public String password;

  @CreationTimestamp
  private LocalDateTime createdAt;
}
