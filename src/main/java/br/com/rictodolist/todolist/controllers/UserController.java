package br.com.rictodolist.todolist.controllers;

import br.com.rictodolist.todolist.domain.user.UserModel;
import br.com.rictodolist.todolist.services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/")
  public ResponseEntity<UserModel> create(@RequestBody UserModel userModel) {
    var userCreated = this.userService.create(userModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
  }
}
