package model;

import connexion.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Avion {
    private int idAvion;
    private int idCompagnie;
    private String modele;
    private int capacite;
    private int anneeFabrication;
    private String statut;

    // Constructeurs
    public Avion() {
    }

    public Avion(int idAvion, int idCompagnie, String modele, int capacite, int anneeFabrication, String statut) {
        this.idAvion = idAvion;
        this.idCompagnie = idCompagnie;
        this.modele = modele;
        this.capacite = capacite;
        this.anneeFabrication = anneeFabrication;
        this.statut = statut;
    }

    // Getters et Setters
    public int getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(int idAvion) {
        this.idAvion = idAvion;
    }

    public int getIdCompagnie() {
        return idCompagnie;
    }

    public void setIdCompagnie(int idCompagnie) {
        this.idCompagnie = idCompagnie;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public int getAnneeFabrication() {
        return anneeFabrication;
    }

    public void setAnneeFabrication(int anneeFabrication) {
        this.anneeFabrication = anneeFabrication;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public static List<Avion> findAll() {
        List<Avion> avions = new ArrayList<>();
        String sql = "SELECT * FROM Avion";

        try (Connection connex = ConnectionDB.getConnection();
             PreparedStatement stmt = connex.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Avion avion = new Avion(
                rs.getInt("id_avion"),
                rs.getInt("id_compagnie"),
                rs.getString("modele"),
                rs.getInt("capacite"),
                rs.getInt("annee_fabrication"),
                rs.getString("statut")
                );
                avions.add(avion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return avions;
    }
}