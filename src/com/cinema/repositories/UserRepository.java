package com.cinema.repositories;

import com.cinema.database.DatabaseConnection;
import com.cinema.models.User;

import java.sql.*;

public class UserRepository {
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            return map(rs);
        }
    }

    public User createCustomer(String email, String hash, String fn, String ln) throws SQLException {
        String sql = "INSERT INTO users(email,password_hash,first_name,last_name,role) " +
                "VALUES (?,?,?,?, 'CUSTOMER') RETURNING *";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, hash);
            ps.setString(3, fn);
            ps.setString(4, ln);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return map(rs);
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setFirstName(rs.getString("first_name"));
        u.setLastName(rs.getString("last_name"));
        u.setRole(rs.getString("role"));
        return u;
    }
}
