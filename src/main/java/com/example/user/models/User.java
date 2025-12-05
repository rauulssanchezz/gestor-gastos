package com.example.user.models;

import com.example.utils.Utils;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class User {

    @NotBlank(message = "Falta uno de los campos necesarios.")
    @Email(message = "El formato de émail es incorrecto")
    private String email;

    @NotBlank(message = "Falta uno de los campos necesarios.")
    private String password;

    private String name;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String plainPassword) {
        this.password = Utils.hashPassword(plainPassword);
    }

    public Boolean checkPassword(String plainPassword) {
        return Utils.checkPassword(plainPassword, this.password);
    }
}