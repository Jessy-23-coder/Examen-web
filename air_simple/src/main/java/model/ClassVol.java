package model;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connexion.ConnectionDB;

public class ClassVol {
    private int idClasseVol;
    private int idVol;
    private int idClasse;
    private double prixAdulte;
    private double prixEnfant;
    private int placesDisponibles;
    private String nomClasse;
    
    private double prixInitialAdulte;
    private double prixInitialEnfant;
    private LocalDateTime dateActivation;
    private int delaisJours;
    private double pourcentageAugmentation;

    // Constructeurs
    public ClassVol() {
        this.delaisJours = 1; // Valeur par défaut: 1 jour
        this.pourcentageAugmentation = 100; // 100% par défaut
    }

    public ClassVol(int idVol, int idClasse, double prixAdulte, double prixEnfant, int placesDisponibles) {
        this();
        this.idVol = idVol;
        this.idClasse = idClasse;
        this.prixAdulte = prixAdulte;
        this.prixEnfant = prixEnfant;
        this.placesDisponibles = placesDisponibles;
        this.prixInitialAdulte = prixAdulte;
        this.prixInitialEnfant = prixEnfant;
        this.dateActivation = LocalDateTime.now();
    }

    // Getters et Setters
    public int getIdClasseVol() { return idClasseVol; }
    public void setIdClasseVol(int idClasseVol) { this.idClasseVol = idClasseVol; }
    
    public int getIdVol() { return idVol; }
    public void setIdVol(int idVol) { this.idVol = idVol; }
    
    public int getIdClasse() { return idClasse; }
    public void setIdClasse(int idClasse) { this.idClasse = idClasse; }
    
    public double getPrixAdulte() { return prixAdulte; }
    public void setPrixAdulte(double prixAdulte) { this.prixAdulte = prixAdulte; }
    
    public double getPrixEnfant() { return prixEnfant; }
    public void setPrixEnfant(double prixEnfant) { this.prixEnfant = prixEnfant; }
    
    public int getPlacesDisponibles() { return placesDisponibles; }
    public void setPlacesDisponibles(int placesDisponibles) { this.placesDisponibles = placesDisponibles; }
    
    public String getNomClasse() { return nomClasse; }
    public void setNomClasse(String nomClasse) { this.nomClasse = nomClasse; }
    
    public double getPrixInitialAdulte() { return prixInitialAdulte; }
    public void setPrixInitialAdulte(double prixInitialAdulte) { this.prixInitialAdulte = prixInitialAdulte; }
    
    public double getPrixInitialEnfant() { return prixInitialEnfant; }
    public void setPrixInitialEnfant(double prixInitialEnfant) { this.prixInitialEnfant = prixInitialEnfant; }
    
    public LocalDateTime getDateActivation() { return dateActivation; }
    public void setDateActivation(LocalDateTime dateActivation) { this.dateActivation = dateActivation; }
    
    public int getDelaisJours() { return delaisJours; }
    public void setDelaisJours(int delaisJours) { this.delaisJours = delaisJours; }
    
    public double getPourcentageAugmentation() { return pourcentageAugmentation; }
    public void setPourcentageAugmentation(double pourcentageAugmentation) { 
        this.pourcentageAugmentation = pourcentageAugmentation; 
    }


//    public static void verifierEtMettreAJourTousLesPrix() throws SQLException {
//     String sql = "SELECT * FROM classvol WHERE prix_adulte = prix_initial_adulte " +
//                  "AND date_activation IS NOT NULL " +
//                  "AND NOW() >= DATE_ADD(date_activation, INTERVAL delais_jours DAY)";
    
//     try (Connection conn = ConnectionDB.getConnection();
//          Statement stmt = conn.createStatement();
//          ResultSet rs = stmt.executeQuery(sql)) {
        
//         while (rs.next()) {
//             ClassVol cv = new ClassVol();
//             cv.setIdClasseVol(rs.getInt("id_classevol"));
//             cv.setPrixAdulte(rs.getDouble("prix_adulte"));
//             cv.setPrixEnfant(rs.getDouble("prix_enfant"));
//             cv.setPrixInitialAdulte(rs.getDouble("prix_initial_adulte"));
//             cv.setPrixInitialEnfant(rs.getDouble("prix_initial_enfant"));
//             cv.setPourcentageAugmentation(rs.getDouble("pourcentage_augmentation"));
            
//             // Calculer les nouveaux prix
//             double nouveauPrixAdulte = cv.getPrixInitialAdulte() * (1 + cv.getPourcentageAugmentation()/100);
//             double nouveauPrixEnfant = cv.getPrixInitialEnfant() * (1 + cv.getPourcentageAugmentation()/100);
            
//             // Mettre à jour en base
//             String updateSql = "UPDATE classvol SET prix_adulte = ?, prix_enfant = ? WHERE id_classevol = ?";
//             try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
//                 updateStmt.setDouble(1, nouveauPrixAdulte);
//                 updateStmt.setDouble(2, nouveauPrixEnfant);
//                 updateStmt.setInt(3, cv.getIdClasseVol());
//                 updateStmt.executeUpdate();
//             }
//         }
//     }
// }


public void verifierEtMettreAJourPrix() throws SQLException {
    // Calculer la différence en MINUTES
    long minutesEcoulees = ChronoUnit.MINUTES.between(dateActivation, LocalDateTime.now());
    
    if (minutesEcoulees >= 2 && prixAdulte == prixInitialAdulte) { // Après 2 minutes
        prixAdulte = prixInitialAdulte * 1.5; // +50%
        prixEnfant = prixInitialEnfant * 1.5;
        
        // Mettre à jour en base
        String sql = "UPDATE classvol SET prix_adulte=?, prix_enfant=? WHERE id_classevol=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, prixAdulte);
            stmt.setDouble(2, prixEnfant);
            stmt.setInt(3, idClasseVol);
            stmt.executeUpdate();
        }
    }
}

    // Méthode pour forcer l'augmentation immédiate (pour tests)
    public void forcerAugmentationPrix() throws SQLException {
        prixAdulte = prixInitialAdulte * (1 + pourcentageAugmentation/100);
        prixEnfant = prixInitialEnfant * (1 + pourcentageAugmentation/100);
        updatePrixInDatabase();
    }

    private void updatePrixInDatabase() throws SQLException {
        String sql = "UPDATE classvol SET prix_adulte = ?, prix_enfant = ? WHERE id_classevol = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, prixAdulte);
            stmt.setDouble(2, prixEnfant);
            stmt.setInt(3, idClasseVol);
            stmt.executeUpdate();
        }
    }

    // Méthodes DAO existantes (à adapter avec les nouveaux champs)
    public static ClassVol findById(int idClasseVol) throws SQLException {
        String sql = "SELECT cv.*, c.nom as nom_classe, cv.prix_initial_adulte, cv.prix_initial_enfant, " +
                    "cv.date_activation, cv.delais_jours, cv.pourcentage_augmentation " +
                    "FROM classvol cv JOIN classe c ON cv.id_classe = c.id_classe " +
                    "WHERE cv.id_classevol = ?";
        
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idClasseVol);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ClassVol cv = new ClassVol();
                    cv.setIdClasseVol(rs.getInt("id_classevol"));
                    cv.setIdVol(rs.getInt("id_vol"));
                    cv.setIdClasse(rs.getInt("id_classe"));
                    cv.setPrixAdulte(rs.getDouble("prix_adulte"));
                    cv.setPrixEnfant(rs.getDouble("prix_enfant"));
                    cv.setPlacesDisponibles(rs.getInt("places_disponibles"));
                    cv.setNomClasse(rs.getString("nom_classe"));
                    cv.setPrixInitialAdulte(rs.getDouble("prix_initial_adulte"));
                    cv.setPrixInitialEnfant(rs.getDouble("prix_initial_enfant"));
                    cv.setDateActivation(rs.getTimestamp("date_activation").toLocalDateTime());
                    cv.setDelaisJours(rs.getInt("delais_jours"));
                    cv.setPourcentageAugmentation(rs.getDouble("pourcentage_augmentation"));
                    return cv;
                }
            }
        }
        return null;
    }

    public static List<Classe> getAllClasses() throws SQLException {
            List<Classe> classes = new ArrayList<>();
            String sql = "SELECT * FROM classe";
            
            try (Connection conn = ConnectionDB.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    Classe c = new Classe();
                    c.setIdClasse(rs.getInt("id_classe"));
                    c.setNom(rs.getString("nom"));
                    classes.add(c);
                }
            }
            return classes;
        }
        
        public static Map<Integer, ClassVol> getClassesForVol(int idVol) throws SQLException {
            Map<Integer, ClassVol> classesVol = new HashMap<>();
            String sql = "SELECT * FROM classvol WHERE id_vol = ?";
            
            try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, idVol);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        ClassVol cv = new ClassVol();
                        cv.setIdClasseVol(rs.getInt("id_classevol"));
                        cv.setIdVol(rs.getInt("id_vol"));
                        cv.setIdClasse(rs.getInt("id_classe"));
                        cv.setPrixAdulte(rs.getDouble("prix_adulte"));
                        cv.setPrixEnfant(rs.getDouble("prix_enfant"));
                        cv.setPlacesDisponibles(rs.getInt("places_disponibles"));
                        classesVol.put(cv.getIdClasse(), cv);
                    }
                }
            }
            return classesVol;
        }
        
        public static void saveOrUpdate(ClassVol classVol) throws SQLException {
            if (classVol.getIdClasseVol() > 0) {
                update(classVol);
            } else {
                insert(classVol);
            }
        }
        
        public static void insert(ClassVol classVol) throws SQLException {
            String sql = "INSERT INTO classvol (id_vol, id_classe, prix_adulte, prix_enfant, places_disponibles) " +
                    "VALUES (?, ?, ?, ?, ?)";
            
            try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                
                stmt.setInt(1, classVol.getIdVol());
                stmt.setInt(2, classVol.getIdClasse());
                stmt.setDouble(3, classVol.getPrixAdulte());
                stmt.setDouble(4, classVol.getPrixEnfant());
                stmt.setInt(5, classVol.getPlacesDisponibles());
                
                int affectedRows = stmt.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Échec de l'insertion, aucune ligne affectée.");
                }
                
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        classVol.setIdClasseVol(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Échec de l'insertion, aucun ID obtenu.");
                    }
                }
            }
        }
        
        public static void update(ClassVol classVol) throws SQLException {
            String sql = "UPDATE classvol SET prix_adulte = ?, prix_enfant = ?, places_disponibles = ? " +
                    "WHERE id_classevol = ?";
            
            try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setDouble(1, classVol.getPrixAdulte());
                stmt.setDouble(2, classVol.getPrixEnfant());
                stmt.setInt(3, classVol.getPlacesDisponibles());
                stmt.setInt(4, classVol.getIdClasseVol());
                
                int affectedRows = stmt.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Échec de la mise à jour, aucune ligne affectée.");
                }
            }
        }
        
        public static void delete(int idClasseVol) throws SQLException {
            String sql = "DELETE FROM classvol WHERE id_classevol = ?";
            
            try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, idClasseVol);
                
                int affectedRows = stmt.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Échec de la suppression, aucune ligne affectée.");
                }
            }
        }

        // Méthode utilitaire pour trouver par vol et classe
        public static ClassVol findByVolAndClass(int idVol, int idClasse) throws SQLException {
            String sql = "SELECT * FROM classvol WHERE id_vol = ? AND id_classe = ?";
            
            try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, idVol);
                stmt.setInt(2, idClasse);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        ClassVol cv = new ClassVol();
                        cv.setIdClasseVol(rs.getInt("id_classevol"));
                        cv.setIdVol(rs.getInt("id_vol"));
                        cv.setIdClasse(rs.getInt("id_classe"));
                        cv.setPrixAdulte(rs.getDouble("prix_adulte"));
                        cv.setPrixEnfant(rs.getDouble("prix_enfant"));
                        cv.setPlacesDisponibles(rs.getInt("places_disponibles"));
                        return cv;
                    }
                }
            }
            return null;
        }
        

    public static List<ClassVol> findByVolId(int idVol) {
        System.out.println("Recherche classes pour vol ID: " + idVol);
        List<ClassVol> classes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionDB.getConnection();
            String sql = "SELECT cv.*, c.nom as nom_classe FROM classvol cv " +
                    "JOIN classe c ON cv.id_classe = c.id_classe " +
                    "WHERE cv.id_vol = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idVol);
            rs = stmt.executeQuery();

            while (rs.next()) {
                ClassVol cv = new ClassVol();
                cv.setIdClasseVol(rs.getInt("id_classevol"));
                cv.setIdVol(rs.getInt("id_vol"));
                cv.setIdClasse(rs.getInt("id_classe"));
                cv.setPrixAdulte(rs.getDouble("prix_adulte"));
                cv.setPrixEnfant(rs.getDouble("prix_enfant"));
                cv.setPlacesDisponibles(rs.getInt("places_disponibles"));
                cv.setNomClasse(rs.getString("nom_classe")); 
                
                classes.add(cv);
                
                System.out.println("Classe trouvée: " + cv.getIdClasse() + " - " + cv.getNomClasse());
            }
            return classes;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            // Fermez les ressources
        }
    }

    public void mettreAJourPrixSiDelaisDepasse() throws SQLException {
        long joursEcoules = ChronoUnit.DAYS.between(this.dateActivation, LocalDateTime.now());
        
        if (joursEcoules >= this.delaisJours && this.prixAdulte == this.prixInitialAdulte) {
            this.prixAdulte = this.prixInitialAdulte * 2;
            this.prixEnfant = this.prixInitialEnfant * 2;
            
            // Mettre à jour en base
            String sql = "UPDATE classvol SET prix_adulte = ?, prix_enfant = ? WHERE id_classevol = ?";
            try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, this.prixAdulte);
                stmt.setDouble(2, this.prixEnfant);
                stmt.setInt(3, this.idClasseVol);
                stmt.executeUpdate();
            }
        }
    }


    public static void initialiserAugmentationPrix(int idVol) throws SQLException {
        String sql;
        if (idVol > 0) {
            sql = "UPDATE classvol SET " +
                "prix_initial_adulte = prix_adulte, " +
                "prix_initial_enfant = prix_enfant, " +
                "date_activation = NOW(), " +
                "delais_jours = 1, " +
                "pourcentage_augmentation = 100 " +
                "WHERE id_vol = ?";
        } else {
            sql = "UPDATE classvol SET " +
                "prix_initial_adulte = prix_adulte, " +
                "prix_initial_enfant = prix_enfant, " +
                "date_activation = NOW(), " +
                "delais_jours = 1, " +
                "pourcentage_augmentation = 100";
        }
        
        try (Connection conn = ConnectionDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (idVol > 0) {
                stmt.setInt(1, idVol);
            }
            
            stmt.executeUpdate();
        }
    }



}

