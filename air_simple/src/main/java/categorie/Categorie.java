package categorie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connexion.ConnectionDB;;

public class Categorie {

    private int id;
    private String name;

    public Categorie() {}

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<Categorie> findAll() throws Exception {
        List<Categorie> list = new ArrayList<>();
        String sql = "SELECT * FROM categorie";

        try (Connection connex = ConnectionDB.getConnection();
             PreparedStatement stmt = connex.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Categorie cat = new Categorie();
                cat.setID(rs.getInt("id"));
                cat.setName(rs.getString("nom"));
                list.add(cat);
            }

        } catch (Exception e) {
            throw new Exception("Erreur lors de la récupération des catégories : " + e.getMessage(), e);
        }

        return list;
    }

     // mamorona
    public static void insertCategorie(String nom) throws Exception {
        String sql = "INSERT INTO categorie(nom) VALUES(?)";

        try (Connection connex = ConnectionDB.getConnection();
             PreparedStatement stmt = connex.prepareStatement(sql)) {

            stmt.setString(1, nom);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Erreur lors de l'insertion de la catégorie : " + e.getMessage(), e);
        }
    }

    // manova 
    public static void updateCategorie(int id, String nouveauNom) throws Exception {
        String sql = "UPDATE categorie SET nom = ? WHERE id = ?";

        try (Connection connex = ConnectionDB.getConnection();
            PreparedStatement stmt = connex.prepareStatement(sql)) {

            stmt.setString(1, nouveauNom);
            stmt.setInt(2, id);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated == 0) {
                throw new Exception("Aucune catégorie trouvée avec l'ID : " + id);
            }

        } catch (Exception e) {
            throw new Exception("Erreur lors de la mise à jour de la catégorie : " + e.getMessage(), e);
        }
    }

    // mamafa 
    public static void deleteCategorie(int id) throws Exception {
        String sql = "DELETE FROM categorie WHERE id = ?";

        try (Connection connex = ConnectionDB.getConnection();
            PreparedStatement stmt = connex.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted == 0) {
                throw new Exception("Aucune catégorie trouvée avec l'ID : " + id);
            }

        } catch (Exception e) {
            throw new Exception("Erreur lors de la suppression de la catégorie : " + e.getMessage(), e);
        }
    }

    public static Categorie findById(int id) throws Exception {
        String sql = "SELECT * FROM categorie WHERE id = ?";
        try (Connection connex = ConnectionDB.getConnection();
            PreparedStatement stmt = connex.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Categorie cat = new Categorie();
                    cat.setID(rs.getInt("id"));
                    cat.setName(rs.getString("nom"));
                    return cat;
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            throw new Exception("Erreur lors de la récupération de la catégorie : " + e.getMessage(), e);
        }
    }
}
