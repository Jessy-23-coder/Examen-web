import connexion.ConnectionDB;
import model.VueVol;
import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Test de la connexion à la base
        testConnexion();
        
        // 2. Test de la récupération des vols
        testFindAllVols();
    }

    private static void testConnexion() {
        System.out.println("=== TEST DE CONNEXION ===");
        try (Connection conn = ConnectionDB.getConnection()) {
            System.out.println("✅ Connexion à la base de données réussie !");
            System.out.println("URL: " + conn.getMetaData().getURL());
            System.out.println("Utilisateur: " + conn.getMetaData().getUserName());
        } catch (Exception e) {
            System.err.println("❌ Erreur de connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testFindAllVols() {
        System.out.println("\n=== TEST RÉCUPÉRATION DES VOLS ===");
        try {
            List<VueVol> vols = VueVol.findAll();   
            
            if (vols.isEmpty()) {
                System.out.println("Aucun vol trouvé dans la base de données.");
                return;
            }
            
            System.out.println("Nombre de vols récupérés : " + vols.size());
            System.out.println("\nListe des vols :");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-8s | %-15s | %-20s | %-15s | %-20s | %-20s | %-10s |\n", 
                "ID Vol", "Numéro Vol", "Compagnie", "Départ", "Arrivée", "Date Départ", "Statut");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
            
            for (VueVol vol : vols) {
                System.out.printf("| %-8d | %-15s | %-20s | %-15s | %-20s | %-20s | %-10s |\n",
                    vol.getIdVol(),
                    vol.getNumeroVol(),
                    vol.getCompagnieAerienne(),
                    vol.getCodeDepart(),
                    vol.getCodeArrivee(),
                    vol.getDateHeureDepart(),
                    vol.getStatut());
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des vols : " + e.getMessage());
            e.printStackTrace();
        }
    }
}