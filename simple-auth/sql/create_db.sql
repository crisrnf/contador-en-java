-- SQL script to create database and users table for SimpleAuth

CREATE DATABASE IF NOT EXISTS simple_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE simple_auth;

CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
