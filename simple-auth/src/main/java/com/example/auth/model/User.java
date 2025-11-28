package com.example.auth.model;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String usuario; // username
    private String email;
    private String passwordHash; // contrasena hash
    private LocalDateTime createdAt;
    private Integer idRol;
    private double dineroAhorrado;

    public User() {}

    public User(String email, String passwordHash) {
        this.usuario = email; // default username = email unless specified
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Integer getIdRol() { return idRol; }
    public void setIdRol(Integer idRol) { this.idRol = idRol; }

    public double getDineroAhorrado() { return dineroAhorrado; }
    public void setDineroAhorrado(double dineroAhorrado) { this.dineroAhorrado = dineroAhorrado; }
}
