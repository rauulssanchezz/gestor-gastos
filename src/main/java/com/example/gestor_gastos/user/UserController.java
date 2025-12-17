package com.example.gestor_gastos.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestor_gastos.user.models.User;
import com.example.gestor_gastos.user.models.UserCredentials;
import com.example.gestor_gastos.user.models.UserUpdate;

import jakarta.servlet.http.HttpServletRequest;


@RestController
public class UserController {
    
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user")
    public void createUser(@RequestBody User user) {
        this.userService.createUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/user")
    public void login(@RequestBody UserCredentials user) {
        this.userService.login(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/auth/user/update")
    public void updateUser(@RequestBody UserUpdate user, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");

        user.setId(userId);

        this.userService.updateUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/auth/user/logout")
    public void logout(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);

        this.userService.logout(token);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/auth/user/delete")
    public void deleteUser(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");

        this.userService.deleteUser(userId);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    
}
