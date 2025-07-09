package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connexion.ConnectionDB;

public class Aeroport {
    private int idAeroport;
    private String nom;
    private String codeIATA;
    private String ville;
    private String pays;
    private String terminal;

    // Constructors
    public Aeroport() {
    }

    public Aeroport(int idAeroport, String nom, String codeIATA, String ville, String pays, String terminal) {
        this.idAeroport = idAeroport;
        this.nom = nom;
        this.codeIATA = codeIATA;
        this.ville = ville;
        this.pays = pays;
        this.terminal = terminal;
    }

    // Getters and Setters
    public int getIdAeroport() {
        return idAeroport;
    }

    public void setIdAeroport(int idAeroport) {
        this.idAeroport = idAeroport;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodeIATA() {
        return codeIATA;
    }

    public void setCodeIATA(String codeIATA) {
        this.codeIATA = codeIATA;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    // Method to find all airports from the database
    public static List<Aeroport> findAll() throws SQLException {
        List<Aeroport> aeroports = new ArrayList<>();
        String sql = "SELECT * FROM Aeroport";
        
       try (Connection connex = ConnectionDB.getConnection();
             PreparedStatement stmt = connex.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Aeroport aeroport = new Aeroport(
                    rs.getInt("id_aeroport"),
                    rs.getString("nom"),
                    rs.getString("code_IATA"),
                    rs.getString("ville"),
                    rs.getString("pays"),
                    rs.getString("terminal")
                );
                aeroports.add(aeroport);
            }
        }
        
        return aeroports;
    }

  

}