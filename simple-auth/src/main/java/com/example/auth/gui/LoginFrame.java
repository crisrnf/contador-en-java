package com.example.auth.gui;

import com.example.auth.dao.UserDao;
import com.example.auth.model.User;
import com.example.auth.util.PasswordUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);

    public LoginFrame() {
        setTitle("Iniciar Sesión - SimpleAuth");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Correo electrónico:"), c);
        c.gridx = 1; c.gridy = 0;
        form.add(emailField, c);

        c.gridx = 0; c.gridy = 1;
        form.add(new JLabel("Contraseña:"), c);
        c.gridx = 1; c.gridy = 1;
        form.add(passwordField, c);

        JPanel buttons = new JPanel();
        JButton loginBtn = new JButton("Entrar");
        JButton registerBtn = new JButton("Registrarse");
        buttons.add(loginBtn);
        buttons.add(registerBtn);

        loginBtn.addActionListener(this::onLogin);
        registerBtn.addActionListener(e -> {
            this.setVisible(false);
            new RegisterFrame(this).setVisible(true);
        });

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void onLogin(ActionEvent ev) {
        String email = emailField.getText().trim();
        String pw = new String(passwordField.getPassword());
        if (email.isEmpty() || pw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete email y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            UserDao dao = new UserDao();
            User u = dao.findByEmail(email);
            if (u == null) {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean ok = PasswordUtil.verify(pw, u.getPasswordHash());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Se inicio sesion correctamente", "OK", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean loginProgrammatic(String email, String password) {
        if (email == null || password == null) return false;
        try {
            UserDao dao = new UserDao();
            User u = dao.findByEmail(email);
            if (u == null) {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            boolean ok = PasswordUtil.verify(password, u.getPasswordHash());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Se inicio sesion correctamente", "OK", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}

/*
se puede registrar e iniciar sesion con cualquer usuario que se haga
*/