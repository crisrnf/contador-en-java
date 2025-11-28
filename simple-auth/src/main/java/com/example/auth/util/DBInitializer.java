package com.example.auth.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Loads and executes SQL schema (src/main/resources/db/schema.sql) to ensure tables exist.
 * This class runs on application startup and is safe to call multiple times (schema.sql uses IF NOT EXISTS).
 */
public final class DBInitializer {
    private DBInitializer() { }

    public static void init() {
        // choose schema file depending on db type (H2 uses a different DDL)
        String dbType = DBConnection.getDbType();
        InputStream in = null;
        if (dbType != null && dbType.equals("h2")) {
            in = DBInitializer.class.getResourceAsStream("/db/schema-h2.sql");
        }
        if (in == null) in = DBInitializer.class.getResourceAsStream("/db/schema.sql");
        if (in == null) in = DBInitializer.class.getResourceAsStream("/schema.sql");
        if (in == null) return; // no schema file
        try (InputStream is = in) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            }

            // split on ';' and execute statements one by one
            String[] statements = sb.toString().split(";");
            try (Connection conn = DBConnection.getConnection(); Statement st = conn.createStatement()) {
                for (String s : statements) {
                    String sql = s.trim();
                    if (sql.isEmpty()) continue;
                    // Skip statements that are not needed for H2 embedded (CREATE DATABASE / USE)
                    if (dbType != null && dbType.equals("h2")) {
                        String upper = sql.toUpperCase();
                        if (upper.startsWith("CREATE DATABASE") || upper.startsWith("USE ")) {
                            continue;
                        }
                    }
                    try {
                        st.execute(sql);
                    } catch (Exception ex) {
                        // ignore individual statement failures (seed inserts may fail if already present)
                        // print stack for debugging
                        System.err.println("DBInitializer: failed to execute statement: " + ex.getMessage());
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("DBInitializer init error: " + ex.getMessage());
        }
    }
}
