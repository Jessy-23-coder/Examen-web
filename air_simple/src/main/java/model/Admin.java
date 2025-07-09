package model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connexion.ConnectionDB;


public class Admin {
    private int idAdmin;
    private String mail;
    private String passe;

    // Constructeurs
    public Admin() {}

    public Admin(String mail, String passe) {
        this.mail = mail;
        this.passe = passe;
    }

    // Getters et Setters
    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPasse() {
        return passe;
    }

    public void setPasse(String passe) {
        this.passe = passe;
    }

    // Méthode d'authentification
    public static Admin authenticate(String mail, String password) throws SQLException {
        String sql = "SELECT * FROM admin WHERE mail = ? AND passe = ?";
        
        try (Connection connex = ConnectionDB.getConnection();
            PreparedStatement stmt = connex.prepareStatement(sql)) {
            
            stmt.setString(1, mail);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Admin admin = new Admin();
                    admin.setIdAdmin(rs.getInt("id_admin"));
                    admin.setMail(rs.getString("mail"));
                    return admin;
                }
            }
        }
        return null;
    }
}