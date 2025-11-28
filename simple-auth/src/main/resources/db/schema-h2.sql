-- H2-compatible DB schema for simple-auth app
-- Creates Personas table used by UserDao/User model

CREATE TABLE IF NOT EXISTS Personas (
  id IDENTITY PRIMARY KEY,
  usuario VARCHAR(150) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  contrasena VARCHAR(255) NOT NULL,
  id_rol INTEGER,
  dinero_ahorrado DECIMAL(15,2) DEFAULT 0.00,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- You can add seed rows here for testing e.g.:
-- INSERT INTO Personas (usuario, email, contrasena, id_rol, dinero_ahorrado) VALUES ('admin', 'admin@example.com', '<HASHED_PASSWORD>', 1, 0.00);
-- H2-compatible DB schema for 'Contador' app

-- 1) Roles
CREATE TABLE IF NOT EXISTS Roles (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL UNIQUE
);

-- 2) Personas
CREATE TABLE IF NOT EXISTS Personas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  usuario VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(150) NOT NULL UNIQUE,
  contrasena VARCHAR(512) NOT NULL,
  id_rol INT DEFAULT NULL,
  dinero_ahorrado DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_rol) REFERENCES Roles(id) ON DELETE SET NULL
);

-- 3) TutoresXAdictos
CREATE TABLE IF NOT EXISTS TutoresXAdictos (
  id_tutoria INT AUTO_INCREMENT PRIMARY KEY,
  id_adicto INT NOT NULL,
  id_tutor INT NOT NULL,
  fecha DATE NOT NULL,
  FOREIGN KEY (id_adicto) REFERENCES Personas(id) ON DELETE CASCADE,
  FOREIGN KEY (id_tutor) REFERENCES Personas(id) ON DELETE CASCADE
);

-- 4) Contadores
CREATE TABLE IF NOT EXISTS Contadores (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario INT NOT NULL,
  fecha_inicio TIMESTAMP NOT NULL,
  fecha_fin TIMESTAMP DEFAULT NULL,
  en_curso BOOLEAN NOT NULL DEFAULT TRUE,
  FOREIGN KEY (id_usuario) REFERENCES Personas(id) ON DELETE CASCADE
);

-- 5) Objetivos
CREATE TABLE IF NOT EXISTS Objetivos (
  id_objetivo INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario INT NOT NULL,
  descripcion TEXT NOT NULL,
  adiccion VARCHAR(100) DEFAULT NULL,
  estado VARCHAR(50) DEFAULT 'pendiente',
  FOREIGN KEY (id_usuario) REFERENCES Personas(id) ON DELETE CASCADE
);

-- 6) Medallas
CREATE TABLE IF NOT EXISTS Medallas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  descripcion TEXT,
  dias_requeridos INT NOT NULL
);

-- 7) usuariosXmedallas
CREATE TABLE IF NOT EXISTS usuariosXmedallas (
  id_usuario_medalla INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario INT NOT NULL,
  id_medalla INT NOT NULL,
  fecha DATE NOT NULL,
  FOREIGN KEY (id_usuario) REFERENCES Personas(id) ON DELETE CASCADE,
  FOREIGN KEY (id_medalla) REFERENCES Medallas(id) ON DELETE CASCADE
);

-- 8) Contactos de emergencia
CREATE TABLE IF NOT EXISTS Contactos_Emergencia (
  id_contacto INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario INT NOT NULL,
  nombre_Contacto VARCHAR(150) NOT NULL,
  telefono_Contacto VARCHAR(50) NOT NULL,
  FOREIGN KEY (id_usuario) REFERENCES Personas(id) ON DELETE CASCADE
);

-- 9) Notificaciones
CREATE TABLE IF NOT EXISTS Notificaciones (
  id INT AUTO_INCREMENT PRIMARY KEY,
  id_destinatario INT NOT NULL,
  mensaje TEXT NOT NULL,
  tipo VARCHAR(50) NOT NULL,
  Fecha_Envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  leida BOOLEAN NOT NULL DEFAULT FALSE,
  FOREIGN KEY (id_destinatario) REFERENCES Personas(id) ON DELETE CASCADE
);

-- seed roles
MERGE INTO Roles (id, nombre) KEY(nombre) VALUES (1, 'adicto');
MERGE INTO Roles (id, nombre) KEY(nombre) VALUES (2, 'tutor');
