package com.example.user;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.user.models.User;
import com.example.user.models.UserCredentials;
import com.example.user.models.UserUpdate;
import com.example.utils.JwtUtil;

import io.jsonwebtoken.Claims;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public void createUser(User user) {
       int rowsAffected = userRepository.createUser(user);

        if (rowsAffected <= 0) {
            throw new RuntimeException("No se pudo crear el usuario"); 
        }
    }

    public String login(UserCredentials user) {
        Map<String, String> userData = userRepository.login(user);

         if (userData == null || userData.isEmpty()) {
            throw new RuntimeException("Usuario no válido.");
        }

        for (String value: userData.values()) {
            if (value == null || value.trim().isEmpty()) {
                throw new RuntimeException("Usuario no válido.");
            }
        }

        return jwtUtil.generateToken(userData.get("id"), userData);
    }

    public void logout(String token) {
        Claims claims = jwtUtil.getClaims(token);
        
        String jti = claims.getId();
        long expirationTime = claims.getExpiration().getTime();

        long now = System.currentTimeMillis();
        long ttlMili = expirationTime - now;
        
        if (ttlMili > 0) {
            long ttlSegundos = ttlMili / 1000;
            jwtUtil.blacklistToken(jti, ttlSegundos);
        }
    }

    public int updateUser(UserUpdate userUpdate) {
        if (!userUpdate.getPassword().trim().isEmpty()) {
            userUpdate.setPassword(userUpdate.getPassword());
        }

        int rowsAffected = this.userRepository.updateUser(userUpdate);

        if (rowsAffected == 0) {
            throw new RuntimeException("Usuario no encontrado o no se puede actualizar.");
        }

        return rowsAffected;
    }

    public int deleteUser(String id) {
        int rowsAffected = this.userRepository.deleteUser(id);

        if (rowsAffected == 0) {
            throw new RuntimeException("Usuario no encontrado o no se puede eliminar.");
        }

        return rowsAffected;
    }

}
