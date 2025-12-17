package com.example.gestor_gastos.user;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.gestor_gastos.user.models.User;
import com.example.gestor_gastos.user.models.UserCredentials;
import com.example.gestor_gastos.user.models.UserUpdate;

import java.util.Map;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public int createUser(User user) {
        String sql = "INSERT INTO users (name, email, password) VALUES(?, ?, ?)";

        try {
            return jdbc.update(sql, user.getName(), user.getEmail(), user.getPassword());
        } catch (DataAccessException ex) {
            System.err.printf("Error al crear usuario (email=%s): %s%n", user.getEmail(), ex.getMessage());
            ex.printStackTrace(System.err);
            throw new RuntimeException("No se pudo crear el usuario", ex);
        }
    }

    public Map<String, String> login(UserCredentials userCredentials) {
        String sql = "SELECT id, email, password FROM users WHERE email = ?";

        try {
            Map<String, Object> rows = jdbc.queryForMap(sql, userCredentials.getEmail());

            Map<String, String> userData = new java.util.HashMap<>();
            userData.put("id", String.valueOf(rows.get("id")));
            userData.put("email", rows.get("email").toString());
            userData.put("password", rows.get("password").toString());

            Boolean passwordsMatch = userCredentials.checkPassword(userData.get("password"));

            if (!passwordsMatch) {
                throw new RuntimeException("Credenciales incorrectas.");
            }

            return userData;
        } catch (DataAccessException ex) {
            System.err.printf("Error al obtener usuario (email=%s): %s%n", userCredentials.getEmail(), ex.getMessage());
            ex.printStackTrace(System.err);
            throw new RuntimeException("No se pudo obtener el usuario", ex);
        } catch (RuntimeException ex) {
            System.err.printf("Error al obtener usuario (email=%s): %s%n", userCredentials.getEmail(), ex.getMessage());
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public int deleteUser(String id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try {
            return jdbc.update(sql, id);
        } catch (DataAccessException ex) {
            System.err.printf("Error al eliminar usuario", ex.getMessage());
            ex.printStackTrace(System.err);
            throw new RuntimeException("No se pudo eliminar el usuario", ex);
        }
    }
    
    public int updateUser(UserUpdate user) {
        String sql = "UPDATE users set name = ?, email = ?, password = ? WHERE id = ?";

        try {
            return jdbc.update(sql, user.getName(), user.getEmail(), user.getPassword(), user.getId());
        } catch (DataAccessException ex) {
            System.err.printf("Error al crear usuario (email=%s): %s%n", user.getEmail(), ex.getMessage());
            ex.printStackTrace(System.err);
            throw new RuntimeException("No se pudo crear el usuario", ex);
        }
    }
}
