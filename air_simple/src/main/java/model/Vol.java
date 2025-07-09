package model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connexion.ConnectionDB;

public class Vol {
    private int idVol;
    private int idAvion;
    private int idAeroportDepart;
    private int idAeroportArrivee;
    private LocalDateTime dateHeureDepart;
    private LocalDateTime dateHeureArrivee;
    private String numeroVol;
    private String statut;
    private String porteEmbarquement;
    private int equipage;

    // Constructors
    public Vol() {
    }

    public Vol(int idVol, int idAvion, int idAeroportDepart, int idAeroportArrivee, 
               LocalDateTime dateHeureDepart, LocalDateTime dateHeureArrivee, String numeroVol, 
               String statut, String porteEmbarquement,int equipage) {
        this.idVol = idVol;
        this.idAvion = idAvion;
        this.idAeroportDepart = idAeroportDepart;
        this.idAeroportArrivee = idAeroportArrivee;
        this.dateHeureDepart = dateHeureDepart;
        this.dateHeureArrivee = dateHeureArrivee;
        this.numeroVol = numeroVol;
        this.statut = statut;
        this.porteEmbarquement = porteEmbarquement;
        this.equipage = equipage;
    }

    // Getters and Setters
    public int getIdVol() {
        return idVol;
    }

    public void setIdVol(int idVol) {
        this.idVol = idVol;
    }

    public int getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(int idAvion) {
        this.idAvion = idAvion;
    }

    public int getIdAeroportDepart() {
        return idAeroportDepart;
    }

    public void setIdAeroportDepart(int idAeroportDepart) {
        this.idAeroportDepart = idAeroportDepart;
    }

    public int getIdAeroportArrivee() {
        return idAeroportArrivee;
    }

    public void setIdAeroportArrivee(int idAeroportArrivee) {
        this.idAeroportArrivee = idAeroportArrivee;
    }

    public LocalDateTime getDateHeureDepart() {
        return dateHeureDepart;
    }

    public void setDateHeureDepart(LocalDateTime dateHeureDepart2) {
        this.dateHeureDepart = dateHeureDepart2;
    }

    public LocalDateTime getDateHeureArrivee() {
        return dateHeureArrivee;
    }

    public void setDateHeureArrivee(LocalDateTime dateHeureArrivee2) {
        this.dateHeureArrivee = dateHeureArrivee2;
    }

    public String getNumeroVol() {
        return numeroVol;
    }

    public void setNumeroVol(String numeroVol) {
        this.numeroVol = numeroVol;
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

    public int getEquipage() {
        return equipage;
    }

    public void setEquipage(int equipage) {
        this.equipage = equipage;
    }

    

    // Method to get all flights from the database
     public static List<Vol> findAll() throws SQLException {
        List<Vol> vols = new ArrayList<>();
        String sql = "SELECT * FROM Vol";
        
        try (Connection connex = ConnectionDB.getConnection();
             PreparedStatement stmt = connex.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                // Conversion des Timestamp en LocalDateTime
                LocalDateTime depart = rs.getTimestamp("date_heure_depart").toLocalDateTime();
                LocalDateTime arrivee = rs.getTimestamp("date_heure_arrivee").toLocalDateTime();
                
                Vol vol = new Vol(
                    rs.getInt("id_vol"),
                    rs.getInt("id_avion"),
                    rs.getInt("id_aeroport_depart"),
                    rs.getInt("id_aeroport_arrivee"),
                    depart,
                    arrivee,
                    rs.getString("numero_vol"),
                    rs.getString("statut"),
                    rs.getString("porte_embarquement"),
                    rs.getInt("equipage")
                );
                vols.add(vol);
            }
        }
        return vols;
    }


   public static void insert( int idAvion, int idAeroportDepart, int idAeroportArrivee, 
               LocalDateTime dateHeureDepart, LocalDateTime dateHeureArrivee, String numeroVol, 
               String statut, String porteEmbarquement,int equipage) 
                            throws SQLException, Exception {
        
        String sql = "INSERT INTO Vol (id_avion, id_aeroport_depart, id_aeroport_arrivee, "
                + "date_heure_depart, date_heure_arrivee, numero_vol, statut, porte_embarquement,equipage) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connex = ConnectionDB.getConnection();
            PreparedStatement stmt = connex.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Conversion des LocalDateTime en Timestamp
            stmt.setInt(1, idAvion);
            stmt.setInt(2, idAeroportDepart);
            stmt.setInt(3, idAeroportArrivee);
            stmt.setTimestamp(4, Timestamp.valueOf(dateHeureDepart));  
            stmt.setTimestamp(5, Timestamp.valueOf(dateHeureArrivee));  
            stmt.setString(6, numeroVol);
            stmt.setString(7, statut);
            stmt.setString(8, porteEmbarquement);
            stmt.setInt(9, equipage);

            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedKeys.getLong(1);
                }
            }
        } catch (SQLException e) {
            // Gestion spécifique des erreurs SQL
            if (e.getSQLState().equals("23000")) {
                if (e.getMessage().contains("id_avion")) {
                    throw new Exception("L'avion sélectionné n'existe pas");
                } else if (e.getMessage().contains("id_aeroport")) {
                    throw new Exception("Un des aéroports sélectionnés n'existe pas");
                } else if (e.getMessage().contains("CHECK")) {
                    throw new Exception("La date d'arrivée doit être après la date de départ");
                }
            }
            throw new Exception("Erreur lors de l'insertion du vol: " + e.getMessage(), e);
        }
    }

    public static Vol findById(int idVol) throws SQLException {
    String sql = "SELECT * FROM Vol WHERE id_vol = ?";
    Vol vol = null;
    
    try (Connection connex = ConnectionDB.getConnection();
            PreparedStatement stmt = connex.prepareStatement(sql)) {
            
            stmt.setInt(1, idVol);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Conversion des Timestamp en LocalDateTime
                    LocalDateTime depart = rs.getTimestamp("date_heure_depart").toLocalDateTime();
                    LocalDateTime arrivee = rs.getTimestamp("date_heure_arrivee").toLocalDateTime();
                    
                    vol = new Vol(
                        rs.getInt("id_vol"),
                        rs.getInt("id_avion"),
                        rs.getInt("id_aeroport_depart"),
                        rs.getInt("id_aeroport_arrivee"),
                        depart,
                        arrivee,
                        rs.getString("numero_vol"),
                        rs.getString("statut"),
                        rs.getString("porte_embarquement"),
                        rs.getInt("equipage")
                    );
                }
            }
        }
        
        if (vol == null) {
            throw new SQLException("Aucun vol trouvé avec l'ID: " + idVol);
        }
        
        return vol;
    }


    public static void update(Vol vol) throws SQLException {
        String sql = "UPDATE Vol SET "
                + "id_avion = ?, "
                + "id_aeroport_depart = ?, "
                + "id_aeroport_arrivee = ?, "
                + "date_heure_depart = ?, "
                + "date_heure_arrivee = ?, "
                + "numero_vol = ?, "
                + "statut = ?, "
                + "porte_embarquement = ? "
                + "equipage = ? "
                + "WHERE id_vol = ?";
        
        try (Connection connex = ConnectionDB.getConnection();
            PreparedStatement stmt = connex.prepareStatement(sql)) {
            
            stmt.setInt(1, vol.getIdAvion());
            stmt.setInt(2, vol.getIdAeroportDepart());
            stmt.setInt(3, vol.getIdAeroportArrivee());
            stmt.setTimestamp(4, Timestamp.valueOf(vol.getDateHeureDepart()));
            stmt.setTimestamp(5, Timestamp.valueOf(vol.getDateHeureArrivee()));
            stmt.setString(6, vol.getNumeroVol());
            stmt.setString(7, vol.getStatut());
            stmt.setString(8, vol.getPorteEmbarquement());
            stmt.setInt(8, vol.getEquipage());
            stmt.setInt(9, vol.getIdVol());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("Échec de la mise à jour, aucun vol trouvé avec l'ID: " + vol.getIdVol());
            }
            connex.commit();
        }  catch (SQLException e) {
                throw e;
            }
    }



    public static void delete(int idVol) throws SQLException {
        String sql = "DELETE FROM Vol WHERE id_vol = ?";
        
        try (Connection connex = ConnectionDB.getConnection();
            PreparedStatement stmt = connex.prepareStatement(sql)) {
            
            stmt.setInt(1, idVol);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("Échec de la suppression, aucun vol trouvé avec l'ID: " + idVol);
            }
        }
    }


    public static List<Vol> search(String keyword) throws SQLException {
    List<Vol> vols = new ArrayList<>();
    
    String sql = "SELECT * FROM Vol WHERE " +
                 "numero_vol LIKE ? OR " +
                 "statut LIKE ? OR " +
                 "porte_embarquement LIKE ? OR " +
                 "id_vol = ?";
    
    try (Connection connex = ConnectionDB.getConnection();
         PreparedStatement stmt = connex.prepareStatement(sql)) {
        
        // Si le keyword est un nombre, on essaie de le matcher avec l'id_vol
        Integer idVol = null;
        try {
            idVol = Integer.parseInt(keyword);
        } catch (NumberFormatException e) {
            // Ce n'est pas un nombre, on ignore
        }
        
        stmt.setString(1, "%" + keyword + "%");
        stmt.setString(2, "%" + keyword + "%");
        stmt.setString(3, "%" + keyword + "%");
        stmt.setObject(4, idVol != null ? idVol : null, Types.INTEGER);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                LocalDateTime depart = rs.getTimestamp("date_heure_depart").toLocalDateTime();
                LocalDateTime arrivee = rs.getTimestamp("date_heure_arrivee").toLocalDateTime();
                
                Vol vol = new Vol(
                    rs.getInt("id_vol"),
                    rs.getInt("id_avion"),
                    rs.getInt("id_aeroport_depart"),
                    rs.getInt("id_aeroport_arrivee"),
                    depart,
                    arrivee,
                    rs.getString("numero_vol"),
                    rs.getString("statut"),
                    rs.getString("porte_embarquement"),
                    rs.getInt("equipage")
                );
                vols.add(vol);
            }
        }
    }
    
    return vols;
}



public static List<Vol> searchByAirports(int idAeroportDepart, int idAeroportArrivee) throws SQLException {
    List<Vol> vols = new ArrayList<>();
    String sql = "SELECT v.* FROM Vol v " +
                 "WHERE v.id_aeroport_depart = ? AND v.id_aeroport_arrivee = ? " +
                 "ORDER BY v.date_heure_depart";
    
    try (Connection connex = ConnectionDB.getConnection();
         PreparedStatement stmt = connex.prepareStatement(sql)) {
        
        stmt.setInt(1, idAeroportDepart);
        stmt.setInt(2, idAeroportArrivee);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                LocalDateTime depart = rs.getTimestamp("date_heure_depart").toLocalDateTime();
                LocalDateTime arrivee = rs.getTimestamp("date_heure_arrivee").toLocalDateTime();
                
                Vol vol = new Vol(
                    rs.getInt("id_vol"),
                    rs.getInt("id_avion"),
                    rs.getInt("id_aeroport_depart"),
                    rs.getInt("id_aeroport_arrivee"),
                    depart,
                    arrivee,
                    rs.getString("numero_vol"),
                    rs.getString("statut"),
                    rs.getString("porte_embarquement"),
                    rs.getInt("equipage")
                );
                vols.add(vol);
            }
        }
    }
    return vols;
}
   


}