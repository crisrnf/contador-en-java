package com.example.auth.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {
    private static final String CONFIG_FILE = "/config.properties";
    private static String url;
    private static String username;
    private static String password;
    private static String dbType; // 'mysql' or 'h2'

    static {
        loadConfig();
    }

    private DBConnection() { }

    private static void loadConfig() {
        try (InputStream in = DBConnection.class.getResourceAsStream(CONFIG_FILE)) {
            Properties p = new Properties();
            if (in != null) {
                p.load(in);
            }
            dbType = p.getProperty("db.type", "h2").trim().toLowerCase();
            if ("mysql".equals(dbType)) {
                url = p.getProperty("db.url", "jdbc:mysql://localhost:3306/simple_auth?serverTimezone=UTC");
                username = p.getProperty("db.user", "root");
                password = p.getProperty("db.password", "");
            } else { // default to H2 embedded
                // use a local file-based H2 DB under ./data/simple_auth (relative to working dir)
                url = p.getProperty("db.url", "jdbc:h2:./data/simple_auth;AUTO_SERVER=TRUE");
                username = p.getProperty("db.user", "");
                password = p.getProperty("db.password", "");
                dbType = "h2";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        // attempt to load common drivers for convenience (if they are present on classpath)
        try {
            if ("mysql".equals(dbType)) {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } else if ("h2".equals(dbType)) {
                Class.forName("org.h2.Driver");
            }
        } catch (ClassNotFoundException ignored) {
            // driver may be auto-registered via ServiceLoader if jar is present; ignore otherwise
        }
        return DriverManager.getConnection(url, username, password);
    }

    public static String getDbType() { return dbType; }
}
