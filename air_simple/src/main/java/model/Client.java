package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connexion.ConnectionDB;


public class Client {
    private int idClient;
    private String mail;
    private String passe;

    // Constructeurs
    public Client() {}

    public Client(String mail, String passe) {
        this.mail = mail;
        this.passe = passe;
    }

    // Getters et Setters
    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
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
  public static Client authenticate(String mail, String password) throws SQLException {
        String sql = "SELECT * FROM client WHERE mail = ? AND passe = ?";
        
        try (Connection connex = ConnectionDB.getConnection();
            PreparedStatement stmt = connex.prepareStatement(sql)) {
            
            stmt.setString(1, mail);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client();
                    client.setIdClient(rs.getInt("id_client"));
                    client.setMail(rs.getString("mail"));
                    return client;
                }
            }
        }
        return null;
    }

}
