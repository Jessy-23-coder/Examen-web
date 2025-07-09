package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connexion.ConnectionDB;

public class VueVol {
    private int idVol;
    private String numeroVol;
    private String compagnieAerienne;
    private String codeCompagnie;
    private String avionModele;
    private int capacite;
    private String aeroportDepart;
    private String codeDepart;
    private String aeroportArrivee;
    private String codeArrivee;
    private LocalDateTime dateHeureDepart;
    private LocalDateTime dateHeureArrivee;
    private int dureeVolMinutes;
    private String statut;
    private String porteEmbarquement;
    private String villeDepart;
    private String villeArrivee;

    // Constructeurs
    public VueVol() {
    }

    public VueVol(int idVol, String numeroVol, String compagnieAerienne, String codeCompagnie, 
                 String avionModele, int capacite, String aeroportDepart, String codeDepart, 
                 String aeroportArrivee, String codeArrivee, LocalDateTime dateHeureDepart, 
                 LocalDateTime dateHeureArrivee, int dureeVolMinutes, String statut, 
                 String porteEmbarquement, String villeDepart, String villeArrivee) {
        this.idVol = idVol;
        this.numeroVol = numeroVol;
        this.compagnieAerienne = compagnieAerienne;
        this.codeCompagnie = codeCompagnie;
        this.avionModele = avionModele;
        this.capacite = capacite;
        this.aeroportDepart = aeroportDepart;
        this.codeDepart = codeDepart;
        this.aeroportArrivee = aeroportArrivee;
        this.codeArrivee = codeArrivee;
        this.dateHeureDepart = dateHeureDepart;
        this.dateHeureArrivee = dateHeureArrivee;
        this.dureeVolMinutes = dureeVolMinutes;
        this.statut = statut;
        this.porteEmbarquement = porteEmbarquement;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
    }

    // Getters et Setters
    public int getIdVol() {
        return idVol;
    }

    public void setIdVol(int idVol) {
        this.idVol = idVol;
    }

    public String getNumeroVol() {
        return numeroVol;
    }

    public void setNumeroVol(String numeroVol) {
        this.numeroVol = numeroVol;
    }

    public String getCompagnieAerienne() {
        return compagnieAerienne;
    }

    public void setCompagnieAerienne(String compagnieAerienne) {
        this.compagnieAerienne = compagnieAerienne;
    }

    public String getCodeCompagnie() {
        return codeCompagnie;
    }

    public void setCodeCompagnie(String codeCompagnie) {
        this.codeCompagnie = codeCompagnie;
    }

    public String getAvionModele() {
        return avionModele;
    }

    public void setAvionModele(String avionModele) {
        this.avionModele = avionModele;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getAeroportDepart() {
        return aeroportDepart;
    }

    public void setAeroportDepart(String aeroportDepart) {
        this.aeroportDepart = aeroportDepart;
    }

    public String getCodeDepart() {
        return codeDepart;
    }

    public void setCodeDepart(String codeDepart) {
        this.codeDepart = codeDepart;
    }

    public String getAeroportArrivee() {
        return aeroportArrivee;
    }

    public void setAeroportArrivee(String aeroportArrivee) {
        this.aeroportArrivee = aeroportArrivee;
    }

    public String getCodeArrivee() {
        return codeArrivee;
    }

    public void setCodeArrivee(String codeArrivee) {
        this.codeArrivee = codeArrivee;
    }

    public LocalDateTime getDateHeureDepart() {
        return dateHeureDepart;
    }

    public void setDateHeureDepart(LocalDateTime dateHeureDepart) {
        this.dateHeureDepart = dateHeureDepart;
    }

    public LocalDateTime getDateHeureArrivee() {
        return dateHeureArrivee;
    }

    public void setDateHeureArrivee(LocalDateTime dateHeureArrivee) {
        this.dateHeureArrivee = dateHeureArrivee;
    }

    public int getDureeVolMinutes() {
        return dureeVolMinutes;
    }

    public void setDureeVolMinutes(int dureeVolMinutes) {
        this.dureeVolMinutes = dureeVolMinutes;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getPorteEmbarquement() {
        return porteEmbarquement;
    }

    public void setPorteEmbarquement(String porteEmbarquement) {
        this.porteEmbarquement = porteEmbarquement;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public static List<VueVol> findAll() throws Exception {
        List<VueVol> list = new ArrayList<>();
        String sql = "SELECT * FROM Vue_Vols";

        try (Connection connex = ConnectionDB.getConnection();
             PreparedStatement stmt = connex.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                VueVol vol = new VueVol(
                rs.getInt("id_vol"),
                rs.getString("numero_vol"),
                rs.getString("compagnie_aerienne"),
                rs.getString("code_compagnie"),
                rs.getString("avion_modele"),
                rs.getInt("capacite"),
                rs.getString("aeroport_depart"),
                rs.getString("code_depart"),
                rs.getString("aeroport_arrivee"),
                rs.getString("code_arrivee"),
                rs.getTimestamp("date_heure_depart").toLocalDateTime(),
                rs.getTimestamp("date_heure_arrivee").toLocalDateTime(),
                rs.getInt("duree_vol_minutes"),
                rs.getString("statut"),
                rs.getString("porte_embarquement"),
                rs.getString("ville_depart"),
                rs.getString("ville_arrivee")
                );
                list.add(vol);
            }

        } catch (Exception e) {
            throw new Exception("Erreur lors de la récupération des catégories : " + e.getMessage(), e);
        }

        return list;
    }



    public static VueVol findById(int idVol) throws SQLException {
    String sql = "SELECT * FROM Vue_Vols WHERE id_vol = ?";
    VueVol vol = null;
    try (Connection connex = ConnectionDB.getConnection();
            PreparedStatement stmt = connex.prepareStatement(sql)) {
            
            stmt.setInt(1, idVol);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    
                    vol = new VueVol(
                       rs.getInt("id_vol"),
                        rs.getString("numero_vol"),
                        rs.getString("compagnie_aerienne"),
                        rs.getString("code_compagnie"),
                        rs.getString("avion_modele"),
                        rs.getInt("capacite"),
                        rs.getString("aeroport_depart"),
                        rs.getString("code_depart"),
                        rs.getString("aeroport_arrivee"),
                        rs.getString("code_arrivee"),
                        rs.getTimestamp("date_heure_depart").toLocalDateTime(),
                        rs.getTimestamp("date_heure_arrivee").toLocalDateTime(),
                        rs.getInt("duree_vol_minutes"),
                        rs.getString("statut"),
                        rs.getString("porte_embarquement"),
                        rs.getString("ville_depart"),
                        rs.getString("ville_arrivee")
                    );
                }
            }
        }
        
        return vol;
    }
    
 
}