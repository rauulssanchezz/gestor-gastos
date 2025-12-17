package com.example.gestor_gastos.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Utils {
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static Boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
