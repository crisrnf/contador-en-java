package com.example.auth.cli;

import com.example.auth.dao.UserDao;
import com.example.auth.model.User;
import com.example.auth.util.DBInitializer;
import com.example.auth.util.PasswordUtil;

import java.sql.SQLException;


public class UserCLI {
    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
            return;
        }
        String cmd = args[0];
        try {
            DBInitializer.init();
            switch (cmd) {
                case "hash":
                    if (args.length < 2) { System.err.println("hash requires <password>"); usage(); return; }
                    String pw = args[1];
                    System.out.println(PasswordUtil.hash(pw));
                    break;
                case "create":
                    if (args.length < 3) { System.err.println("create requires <email> <password>"); usage(); return; }
                    String email = args[1];
                    String password = args[2];
                    String usuario = (args.length >= 4) ? args[3] : email;
                    Integer idRol = null;
                    if (args.length >= 5) {
                        try { idRol = Integer.valueOf(args[4]); } catch (NumberFormatException ex) { /* ignore */ }
                    }

                    User u = new User(email, PasswordUtil.hash(password));
                    u.setUsuario(usuario);
                    if (idRol != null) u.setIdRol(idRol);

                    UserDao dao = new UserDao();
                    boolean ok = dao.createUser(u);
                    if (ok) {
                        System.out.println("User created successfully with id=" + u.getId());
                    } else {
                        System.err.println("Failed to create user.");
                    }
                    break;
                case "show":
                    if (args.length < 2) { System.err.println("show requires <email>"); usage(); return; }
                    String target = args[1];
                    UserDao dao2 = new UserDao();
                    User found = dao2.findByEmail(target);
                    if (found == null) System.out.println("No user found with email: " + target);
                    else {
                        System.out.println("User found: id=" + found.getId() + ", usuario=" + found.getUsuario() + ", email=" + found.getEmail() + ", idRol=" + found.getIdRol() + ", dinero=" + found.getDineroAhorrado());
                    }
                    break;
                case "list":
                    UserDao dao3 = new UserDao();
                    java.util.List<com.example.auth.model.User> users = dao3.findAll();
                    if (users.isEmpty()) System.out.println("No users found in DB.");
                    else users.forEach(x -> System.out.println("id=" + x.getId() + ", usuario=" + x.getUsuario() + ", email=" + x.getEmail()));
                    break;
                default:
                    usage();
            }
        } catch (SQLException ex) {
            System.err.println("DB error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void usage() {
        System.out.println("UserCLI - helper for tests/DB setup\n");
        System.out.println("Usage:");
        System.out.println("  hash <password>                  - prints a PBKDF2 password hash\n");
        System.out.println("  create <email> <password> [usuario] [id_rol]   - creates a user in Personas\n");
    }
}
