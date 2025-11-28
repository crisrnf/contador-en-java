-- DB schema for 'Contador' app (MySQL-compatible)
-- Tables for roles, personas, tutores x adictos, contadores, objetivos, medallas, usuariosXmedallas, contactos_emergencia, notificaciones

CREATE DATABASE IF NOT EXISTS simple_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE simple_auth;

-- 1) Roles
CREATE TABLE IF NOT EXISTS Roles (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- 2) Personas
CREATE TABLE IF NOT EXISTS Personas (
  id INT PRIMARY KEY AUTO_INCREMENT,
  usuario VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(150) NOT NULL UNIQUE,
  contrasena VARCHAR(512) NOT NULL,
  id_rol INT DEFAULT NULL,
  dinero_ahorrado DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_rol) REFERENCES Roles(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 3) TutoresXAdictos (tutor-adicto relationships)
CREATE TABLE IF NOT EXISTS TutoresXAdictos (
  id_tutoria INT PRIMARY KEY AUTO_INCREMENT,
  id_adicto INT NOT NULL,
  id_tutor INT NOT NULL,
  fecha DATE NOT NULL,
  FOREIGN KEY (id_adicto) REFERENCES Personas(id) ON DELETE CASCADE,
  FOREIGN KEY (id_tutor) REFERENCES Personas(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 4) Contadores
CREATE TABLE IF NOT EXISTS Contadores (
  id INT PRIMARY KEY AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  fecha_inicio DATETIME NOT NULL,
  fecha_fin DATETIME DEFAULT NULL,
  en_curso BOOLEAN NOT NULL DEFAULT TRUE,
  FOREIGN KEY (id_usuario) REFERENCES Personas(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5) Objetivos
CREATE TABLE IF NOT EXISTS Objetivos (
  id_objetivo INT PRIMARY KEY AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  `desc` TEXT NOT NULL,
  adiccion VARCHAR(100) DEFAULT NULL,
  estado VARCHAR(50) DEFAULT 'pendiente',
  FOREIGN KEY (id_usuario) REFERENCES Personas(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 6) Medallas
CREATE TABLE IF NOT EXISTS Medallas (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  `desc` TEXT,
  dias_requeridos INT NOT NULL
) ENGINE=InnoDB;

-- 7) usuariosXmedallas
CREATE TABLE IF NOT EXISTS usuariosXmedallas (
  id_usuario_medalla INT PRIMARY KEY AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  id_medalla INT NOT NULL,
  fecha DATE NOT NULL,
  FOREIGN KEY (id_usuario) REFERENCES Personas(id) ON DELETE CASCADE,
  FOREIGN KEY (id_medalla) REFERENCES Medallas(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 8) Contactos de emergencia
CREATE TABLE IF NOT EXISTS Contactos_Emergencia (
  id_contacto INT PRIMARY KEY AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  nombre_Contacto VARCHAR(150) NOT NULL,
  telefono_Contacto VARCHAR(50) NOT NULL,
  FOREIGN KEY (id_usuario) REFERENCES Personas(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 9) Notificaciones
CREATE TABLE IF NOT EXISTS Notificaciones (
  id INT PRIMARY KEY AUTO_INCREMENT,
  id_destinatario INT NOT NULL,
  mensaje TEXT NOT NULL,
  tipo VARCHAR(50) NOT NULL,
  Fecha_Envio DATETIME DEFAULT CURRENT_TIMESTAMP,
  leida BOOLEAN NOT NULL DEFAULT FALSE,
  FOREIGN KEY (id_destinatario) REFERENCES Personas(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- seed roles in an idempotent way (works both in MySQL and H2)
INSERT INTO Roles (nombre)
SELECT 'adicto' WHERE NOT EXISTS (SELECT 1 FROM Roles WHERE nombre='adicto');
INSERT INTO Roles (nombre)
SELECT 'tutor' WHERE NOT EXISTS (SELECT 1 FROM Roles WHERE nombre='tutor');
