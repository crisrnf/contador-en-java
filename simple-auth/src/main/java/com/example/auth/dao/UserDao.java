package com.example.auth.dao;

import com.example.auth.model.User;
import com.example.auth.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public boolean createUser(User user) throws SQLException {
        // Insert into 'Personas' to match new schema
        String sql = "INSERT INTO Personas (usuario, email, contrasena, id_rol, dinero_ahorrado, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsuario() != null ? user.getUsuario() : user.getEmail());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            if (user.getIdRol() != null) ps.setInt(4, user.getIdRol()); else ps.setNull(4, java.sql.Types.INTEGER);
            ps.setBigDecimal(5, java.math.BigDecimal.valueOf(user.getDineroAhorrado()));
            ps.setTimestamp(6, Timestamp.valueOf(user.getCreatedAt()));
            int affected = ps.executeUpdate();
            if (affected == 0) return false;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) user.setId(rs.getInt(1));
            }
            return true;
        }
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT id, usuario, email, contrasena, id_rol, dinero_ahorrado, created_at FROM Personas";
        List<User> out = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setUsuario(rs.getString("usuario"));
                    u.setEmail(rs.getString("email"));
                    u.setPasswordHash(rs.getString("contrasena"));
                    u.setIdRol(rs.getInt("id_rol"));
                    u.setDineroAhorrado(rs.getDouble("dinero_ahorrado"));
                    Timestamp t = rs.getTimestamp("created_at");
                    if (t != null) u.setCreatedAt(t.toLocalDateTime());
                    out.add(u);
                }
            }
        }
        return out;
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT id, usuario, email, contrasena, id_rol, dinero_ahorrado, created_at FROM Personas WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setUsuario(rs.getString("usuario"));
                    u.setEmail(rs.getString("email"));
                    u.setPasswordHash(rs.getString("contrasena"));
                    u.setIdRol(rs.getInt("id_rol"));
                    u.setDineroAhorrado(rs.getDouble("dinero_ahorrado"));
                    Timestamp t = rs.getTimestamp("created_at");
                    if (t != null) u.setCreatedAt(t.toLocalDateTime());
                    return u;
                }
            }
        }
        return null;
    }
}
