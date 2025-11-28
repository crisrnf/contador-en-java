package com.example.auth;

import com.example.auth.gui.LoginFrame;
import com.example.auth.util.DBInitializer;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Initialize DB schema (creates DB/tables if necessary)
        try {
            DBInitializer.init();
        } catch (Exception ex) {
            // log and continue -- GUI will still start but DB ops may fail
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
