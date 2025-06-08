package com.example.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
// import java.io.File;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:contacts.db";
    /*Borrar 11 y 12 solo si es necesario. 11 es la ruta de la BD y 12 es la ruta de la BD en el home del usuario.*/
    //Tener cuidado al borrar 11 y 12
    // private static final String DB_URL = "jdbc:sqlite:" + 
    // System.getProperty("user.home") + "/contacts.db";
    
    public static void initialize() {
        // System.out.println("Working Directory: " + System.getProperty("user.dir"));
        // System.out.println("Ruta de la BD: " + new File("contacts.db").getAbsolutePath());

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            // Tabla usuarios
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL)");
            
            // Tabla contactos
            stmt.execute("CREATE TABLE IF NOT EXISTS contacts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "phone TEXT NOT NULL," +
                    "name TEXT NOT NULL," +
                    "email TEXT," +
                    "business TEXT," +
                    "country TEXT)");
            
            // Insertar usuario demo
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT OR IGNORE INTO users (username, password) VALUES (?, ?)")) {
                pstmt.setString(1, "admin");
                pstmt.setString(2, "admin123");
                pstmt.executeUpdate();
            }
            
        } catch (SQLException e) {
            System.err.println("Error inicializando DB: " + e.getMessage());
        }
    }
    
    public static boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            return pstmt.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Error en login: " + e.getMessage());
            return false;
        }
    }
    
    public static List<Contact> getContacts(String nameFilter, String countryFilter) {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts WHERE name LIKE ? AND country LIKE ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + nameFilter + "%");
            pstmt.setString(2, "%" + countryFilter + "%");
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                contacts.add(new Contact(
                    rs.getInt("id"),
                    rs.getString("phone"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("business"),
                    rs.getString("country")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo contactos: " + e.getMessage());
        }
        return contacts;
    }
    
    public static void saveContact(Contact contact) {
        String sql;
        if (contact.getId() == 0) {
            sql = "INSERT INTO contacts (phone, name, email, business, country) VALUES (?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE contacts SET phone=?, name=?, email=?, business=?, country=? WHERE id=?";
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, contact.getPhone());
            pstmt.setString(2, contact.getName());
            pstmt.setString(3, contact.getEmail());
            pstmt.setString(4, contact.getBusiness());
            pstmt.setString(5, contact.getCountry());
            
            if (contact.getId() != 0) {
                pstmt.setInt(6, contact.getId());
            }
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error guardando contacto: " + e.getMessage());
        }
    }
    
    public static void deleteContact(int id) {
        String sql = "DELETE FROM contacts WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error eliminando contacto: " + e.getMessage());
        }
    }
}
