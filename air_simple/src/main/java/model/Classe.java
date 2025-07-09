package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connexion.ConnectionDB;

public class Classe {
    private int idClasse;
    private String nom;
    
    public Classe(){};

    public Classe(int idClasse, String nom) {
        this.idClasse = idClasse;
        this.nom = nom;
    }

    // Getters et Setters
    public int getIdClasse() { return idClasse; }
    public void setIdClasse(int idClasse) { this.idClasse = idClasse; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }


    public static List<Classe> findAll() throws SQLException {
        List<Classe> classes = new ArrayList<>();
        String sql = "SELECT * FROM Classe";
        
        try (Connection connex = ConnectionDB.getConnection();
             PreparedStatement stmt = connex.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Classe classe = new Classe(
                    rs.getInt("id_classe"),
                    rs.getString("nom")
                );
                classes.add(classe);
            }
        }
        return classes;
    }


}