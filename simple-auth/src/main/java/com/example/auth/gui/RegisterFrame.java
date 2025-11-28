package com.example.auth.gui;

import com.example.auth.dao.UserDao;
import com.example.auth.model.User;
import com.example.auth.util.PasswordUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class RegisterFrame extends JFrame {
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmField = new JPasswordField(20);
    private final JFrame previous;

    public RegisterFrame(JFrame previous) {
        this.previous = previous;
        setTitle("Registro - SimpleAuth");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        c.gridx = 0; c.gridy = 2;
        form.add(new JLabel("Confirmar contraseña:"), c);
        c.gridx = 1; c.gridy = 2;
        form.add(confirmField, c);

        JPanel buttons = new JPanel();
        JButton registerBtn = new JButton("Crear cuenta");
        JButton cancelBtn = new JButton("Cancelar");
        buttons.add(registerBtn);
        buttons.add(cancelBtn);

        registerBtn.addActionListener(this::onRegister);
        cancelBtn.addActionListener(e -> {
            this.dispose();
            if (previous != null) previous.setVisible(true);
        });

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void onRegister(ActionEvent ev) {
        String email = emailField.getText().trim();
        String pw = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (email.isEmpty() || pw.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!pw.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (pw.length() < 6) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            UserDao dao = new UserDao();
            if (dao.findByEmail(email) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un usuario con ese correo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            User u = new User(email, PasswordUtil.hash(pw));
            boolean created = dao.createUser(u);
            if (created) {
                // account created - auto-login if previous is a LoginFrame
                this.dispose();
                if (previous != null) previous.setVisible(true);
                try {
                    if (previous instanceof com.example.auth.gui.LoginFrame) {
                        com.example.auth.gui.LoginFrame lf = (com.example.auth.gui.LoginFrame) previous;
                        boolean logged = lf.loginProgrammatic(email, pw);
                        if (!logged) {
                            JOptionPane.showMessageDialog(this, "Cuenta creada correctamente. Puedes iniciar sesión.", "OK", JOptionPane.INFORMATION_MESSAGE);
                        }
                        return;
                    }
                } catch (Exception ex) {
                    // ignore and show success message below
                }
                JOptionPane.showMessageDialog(this, "Cuenta creada correctamente. Puedes iniciar sesión.", "OK", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear la cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
