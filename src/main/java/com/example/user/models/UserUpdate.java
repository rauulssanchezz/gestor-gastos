package com.example.user.models;

import jakarta.validation.constraints.NotBlank;

public class UserUpdate extends User {

    @NotBlank(message = "Falta uno de los campos necesarios.")
    private String id;

    public UserUpdate(String id, String name, String email, String password) {
        super(name, email, password);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
