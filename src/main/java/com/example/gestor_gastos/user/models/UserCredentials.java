package com.example.gestor_gastos.user.models;

import com.example.gestor_gastos.utils.Utils;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserCredentials {

    @NotBlank(message = "Falta uno de los campos necesarios.")
    @Email(message = "El formato de émail es incorrecto")
    String email;

    @NotBlank(message = "Falta uno de los campos necesarios.")
    String password;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String plainPassword) {
        this.password = Utils.hashPassword(plainPassword);
    }

    public Boolean checkPassword(String hashedPassword) {
        return Utils.checkPassword(this.password, hashedPassword);
    }
}

