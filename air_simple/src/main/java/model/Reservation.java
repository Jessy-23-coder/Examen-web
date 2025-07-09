package model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connexion.ConnectionDB;


public class Reservation {
    private int idReservation;
    private int idVol;
    private LocalDateTime dateReservation;
    private int idClasseVol;
    private int adulte;
    private int enfant;
    private int touslespersonnes;
    private String statut;

    // Constructeurs
    public Reservation() {
    }

    public Reservation(int idVol, int idClasseVol, int adulte, int enfant, String statut) {
        this.idVol = idVol;
        this.idClasseVol = idClasseVol;
        this.adulte = adulte;
        this.enfant = enfant;
        this.touslespersonnes = adulte + enfant; // Calcul automatique
        this.statut = statut;
        this.dateReservation = LocalDateTime.now();
    }

    // Getters et Setters
    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdVol() {
        return idVol;
    }

    public void setIdVol(int idVol) {
        this.idVol = idVol;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public int getIdClasseVol() {
        return idClasseVol;
    }

    public void setIdClasseVol(int idClasseVol) {
        this.idClasseVol = idClasseVol;
    }

    public int getAdulte() {
        return adulte;
    }

    public void setAdulte(int adulte) {
        this.adulte = adulte;
        this.touslespersonnes = this.adulte + this.enfant; // Mise à jour automatique
    }

    public int getEnfant() {
        return enfant;
    }

    public void setEnfant(int enfant) {
        this.enfant = enfant;
        this.touslespersonnes = this.adulte + this.enfant; // Mise à jour automatique
    }

    public int getTouslespersonnes() {
        return touslespersonnes;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    // Méthode pour insérer une réservation
    public static void insert(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO Reservation (id_vol, id_classevol, adulte, enfant, touslespersonnes, statut) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, reservation.getIdVol());
            stmt.setInt(2, reservation.getIdClasseVol());
            stmt.setInt(3, reservation.getAdulte());
            stmt.setInt(4, reservation.getEnfant());
            stmt.setInt(5, reservation.getTouslespersonnes());
            stmt.setString(6, reservation.getStatut());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Échec de la création de la réservation.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setIdReservation(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Échec de la création de la réservation, aucun ID obtenu.");
                }
            }
        }
    }

    // Méthode pour trouver une réservation par ID
    public static Reservation findById(int id) throws SQLException {
        String sql = "SELECT * FROM Reservation WHERE id_reservation = ?";
        Reservation reservation = null;
        
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    reservation = new Reservation();
                    reservation.setIdReservation(rs.getInt("id_reservation"));
                    reservation.setIdVol(rs.getInt("id_vol"));
                    reservation.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                    reservation.setIdClasseVol(rs.getInt("id_classevol"));
                    reservation.setAdulte(rs.getInt("adulte"));
                    reservation.setEnfant(rs.getInt("enfant"));
                    reservation.setStatut(rs.getString("statut"));
                    // touslespersonnes est calculé automatiquement
                }
            }
        }
        
        return reservation;
    }

    // Méthode pour mettre à jour une réservation
    public static void update(Reservation reservation) throws SQLException {
        String sql = "UPDATE Reservation SET id_vol = ?, id_classevol = ?, adulte = ?, " +
                     "enfant = ?, touslespersonnes = ?, statut = ? WHERE id_reservation = ?";
        
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reservation.getIdVol());
            stmt.setInt(2, reservation.getIdClasseVol());
            stmt.setInt(3, reservation.getAdulte());
            stmt.setInt(4, reservation.getEnfant());
            stmt.setInt(5, reservation.getTouslespersonnes());
            stmt.setString(6, reservation.getStatut());
            stmt.setInt(7, reservation.getIdReservation());
            
            stmt.executeUpdate();
        }
    }

    // Méthode pour supprimer une réservation
    public static void delete(int idReservation) throws SQLException {
        String sql = "DELETE FROM Reservation WHERE id_reservation = ?";
        
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idReservation);
            stmt.executeUpdate();
        }
    }



   public static List<Reservation> findAll() throws SQLException {
        System.out.println("Début de findAll()");
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation";

        try (Connection connex = ConnectionDB.getConnection();
            PreparedStatement stmt = connex.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery()) {
            
            System.out.println("Exécution de la requête SQL");
            int count = 0;
            
            while (resultSet.next()) {
                count++;
                Reservation reservation = new Reservation(
                    resultSet.getInt("id_vol"),
                    resultSet.getInt("id_classevol"),
                    resultSet.getInt("adulte"),
                    resultSet.getInt("enfant"),
                    resultSet.getString("statut")
                );
                reservation.setIdReservation(resultSet.getInt("id_reservation"));
                reservation.setDateReservation(resultSet.getTimestamp("date_reservation").toLocalDateTime());
                reservations.add(reservation);
            }
            System.out.println("Nombre de réservations trouvées: " + count);
        }
        return reservations;
    }


public static List<Reservation> searchReservations(String keyword) throws SQLException {
    List<Reservation> reservations = new ArrayList<>();
    String sql = "SELECT * FROM Reservation WHERE " +
                "CAST(id_reservation AS TEXT) LIKE ? OR " +
                "CAST(id_vol AS TEXT) LIKE ? OR " +
                "CAST(id_classevol AS TEXT) LIKE ? OR " +
                "CAST(adulte AS TEXT) LIKE ? OR " +
                "CAST(enfant AS TEXT) LIKE ? OR " +
                "CAST(touslespersonnes AS TEXT) LIKE ? OR " +
                "statut LIKE ?";
    
    try (Connection connex = ConnectionDB.getConnection();
        PreparedStatement stmt = connex.prepareStatement(sql)) {
        
        String searchPattern = "%" + keyword + "%";
        for (int i = 1; i <= 7; i++) {
            stmt.setString(i, searchPattern);
        }
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Reservation reservation = new Reservation(
                    rs.getInt("id_vol"),
                    rs.getInt("id_classevol"),
                    rs.getInt("adulte"),
                    rs.getInt("enfant"),
                    rs.getString("statut")
                );
                reservation.setIdReservation(rs.getInt("id_reservation"));
                reservation.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                reservations.add(reservation);
            }
        }
    }
    return reservations;
}



// Dans Reservation.java
public static List<Reservation> findByVolId(int idVol) throws SQLException {
    List<Reservation> reservations = new ArrayList<>();
    String sql = "SELECT * FROM Reservation WHERE id_vol = ?";
    
    try (Connection conn = ConnectionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, idVol);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setIdReservation(rs.getInt("id_reservation"));
                reservation.setIdVol(rs.getInt("id_vol"));
                reservation.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                reservation.setIdClasseVol(rs.getInt("id_classevol"));
                reservation.setAdulte(rs.getInt("adulte"));
                reservation.setEnfant(rs.getInt("enfant"));
                reservation.setStatut(rs.getString("statut"));
                
                reservations.add(reservation);
            }
        }
    }
    return reservations;
}

public static List<Reservation> findByClasseVolId(int idClasseVol) throws SQLException {
    List<Reservation> reservations = new ArrayList<>();
    String sql = "SELECT * FROM Reservation WHERE id_classevol = ?";
    
    try (Connection conn = ConnectionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, idClasseVol);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setIdReservation(rs.getInt("id_reservation"));
                reservation.setIdVol(rs.getInt("id_vol"));
                reservation.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                reservation.setIdClasseVol(rs.getInt("id_classevol"));
                reservation.setAdulte(rs.getInt("adulte"));
                reservation.setEnfant(rs.getInt("enfant"));
                reservation.setStatut(rs.getString("statut"));
                
                reservations.add(reservation);
            }
        }
    }
    return reservations;
}



// Constantes pour les statuts
public static final String STATUT_ANNULE = "annulé";
public static final String STATUT_NON_PAYE = "non paye";

// Méthode pour annuler une réservation
public static boolean annulerReservation(int idReservation) throws SQLException {
    String sql = "UPDATE Reservation SET statut = ? WHERE id_reservation = ? AND statut != ?";
    
    try (Connection conn = ConnectionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, STATUT_ANNULE);
        stmt.setInt(2, idReservation);
        stmt.setString(3, STATUT_ANNULE); // Pour éviter de ré-annuler une réservation déjà annulée
        
        return stmt.executeUpdate() > 0;
    }
}

// Méthode pour récupérer une réservation avec ses détails
public static Reservation getReservationWithDetails(int idReservation) throws SQLException {
    String sql = "SELECT * FROM Reservation WHERE id_reservation = ?";
    
    try (Connection conn = ConnectionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, idReservation);
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setIdReservation(rs.getInt("id_reservation"));
                reservation.setIdVol(rs.getInt("id_vol"));
                reservation.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                reservation.setIdClasseVol(rs.getInt("id_classevol"));
                reservation.setAdulte(rs.getInt("adulte"));
                reservation.setEnfant(rs.getInt("enfant"));
                reservation.setStatut(rs.getString("statut"));
                return reservation;
            }
        }
    }
    return null;
}



// Ajoutez cette constante
public static final String STATUT_PAYEE = "payée";

// Ajoutez cette méthode
public static boolean payerReservation(int idReservation) throws SQLException {
    String sql = "UPDATE Reservation SET statut = ? WHERE id_reservation = ? AND statut = ?";
    
    try (Connection conn = ConnectionDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, STATUT_PAYEE);
        stmt.setInt(2, idReservation);
        stmt.setString(3, STATUT_NON_PAYE); // Seulement si c'est "non paye"
        
        return stmt.executeUpdate() > 0;
    }
}


}