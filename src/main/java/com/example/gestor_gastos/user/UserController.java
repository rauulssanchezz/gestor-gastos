package com.example.gestor_gastos.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestor_gastos.user.models.User;
import com.example.gestor_gastos.user.models.UserCredentials;


@RestController
public class UserController {
    
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public void createUser(@RequestBody User user) {
        this.userService.createUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/users")
    public void login(@RequestBody UserCredentials user) {
        this.userService.login(user);
    }
}
