package controller;

import java.io.*;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

import model.Aeroport;
import model.Avion;
import model.ClassVol;
import model.Classe;
import model.Reservation;
import model.Vol;
import model.VueVol;

public class VolController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getServletPath();
        
        switch(action) {
            case "/vols":
                afficherListeVols(request, response);
                break;

            case "/ajout_vol":
                afficherFormulaireAjout(request, response);
                break;

            case "/ModifierVol":
                afficherFormulaireModification(request, response);
                break;

            case"/SupprimerVol":
                supprimerVol(request, response);
                break;

            case "/class":
                try {
                    afficherclass(request, response);
                }  catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case"/GestionVols":
                searchByAirports(request, response);
                break;
            
            case"/recherche_resa":
                searchReservations(request, response);
                break;

            case"/liset_reservation":
                affichierreservation(request, response);
                break;

            default:
                response.sendError(404);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getServletPath();
        
        if ("/insert_vol".equals(action)) {
            try {
                insererVol(request, response);
            } catch (ServletException | IOException | SQLException e) {
                e.printStackTrace();
            }
        } else if ("/update_vol".equals(action)) {
            updatevol(request, response);
        }  else if ("/SupprimerVol".equals(action)) {
            supprimerVol(request, response);
        }else if ("/UpdateClassesVol".equals(action)) {
            try {
                traiterClassesVol(request, response);
            } catch (ServletException | IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            response.sendError(404);
        }
    }


// liste reservation
    private void afficherListeVols(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                 List<VueVol> vols = null;
                 List<Aeroport> aeroports = null;
                 try {
                    aeroports = Aeroport.findAll();
                    vols = VueVol.findAll();
                 } catch (Exception e) {
                    e.printStackTrace();
                 }
                request.setAttribute("allaeroports", aeroports);
                request.setAttribute("vuevols", vols);
                request.getRequestDispatcher("/WEB-INF/admin/index.jsp").forward(request, response);
    }
    


    private void afficherclass(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException,Exception {
            try {
            //     int idVol = Integer.parseInt(request.getParameter("idVol"));
                
                List<Classe> toutesClasses = Classe.findAll();
                
            //     request.setAttribute("idVol", idVol);
                request.setAttribute("allClasses", toutesClasses);
                
                request.getRequestDispatcher("/WEB-INF/admin/gestion_classes_vol.jsp").forward(request, response);
                
            } catch (Exception e) {
                e.printStackTrace();
                request.getRequestDispatcher("/WEB-INF/admin/gestion_classes_vol.jsp").forward(request, response);
            } 
    }
    

    private void traiterClassesVol(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException {
    
    try {
        // 1. Récupérer l'ID du vol
        int idVol = Integer.parseInt(request.getParameter("idVol"));
        
        // 2. Récupérer les classes cochées
        String[] classesCochees = request.getParameterValues("classe");
        
        if (classesCochees == null || classesCochees.length == 0) {
            request.setAttribute("erreur", "Veuillez sélectionner au moins une classe");
            request.getRequestDispatcher("/WEB-INF/admin/gestion_classes_vol.jsp").forward(request, response);
            return;
        }
        
        // 3. Pour chaque classe cochée
        for (String idClasseStr : classesCochees) {
            int idClasse = Integer.parseInt(idClasseStr);
            
            // Récupération des valeurs avec les noms de champs spécifiques à la classe
            double prixAdulte = Double.parseDouble(request.getParameter("adulte_" + idClasse));
            double prixEnfant = Double.parseDouble(request.getParameter("enfant_" + idClasse));
            int placesDisponibles = Integer.parseInt(request.getParameter("place_" + idClasse));
            
            // Création de l'objet ClassVol
            ClassVol classVol = new ClassVol();
            classVol.setIdVol(idVol);
            classVol.setIdClasse(idClasse);
            classVol.setPrixAdulte(prixAdulte);
            classVol.setPrixEnfant(prixEnfant);
            classVol.setPlacesDisponibles(placesDisponibles);
            
            // Insertion
            ClassVol.insert(classVol);
        }
        
        // Redirection avec message de succès
        response.sendRedirect(request.getContextPath() + "/vols?success=Classes+ajoutées+avec+succès");
        
    } catch (NumberFormatException e) {
        request.setAttribute("erreur", "Format de nombre invalide: " + e.getMessage());
        request.getRequestDispatcher("/WEB-INF/admin/gestion_classes_vol.jsp").forward(request, response);
    } catch (SQLException e) {
        request.setAttribute("erreur", "Erreur base de données: " + e.getMessage());
        request.getRequestDispatcher("/WEB-INF/admin/gestion_classes_vol.jsp").forward(request, response);
    } catch (Exception e) {
        request.setAttribute("erreur", "Erreur inattendue: " + e.getMessage());
        request.getRequestDispatcher("/WEB-INF/admin/gestion_classes_vol.jsp").forward(request, response);
    }
}


// formulaore ajout vol
    private void afficherFormulaireAjout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                 List<Aeroport> aeroports = null;
                 List<Avion> avions = null;
                 try {
                    aeroports = Aeroport.findAll();
                    avions = Avion.findAll();
                 } catch (Exception e) {
                    e.printStackTrace();
                 }
                request.setAttribute("allaeroports", aeroports);
                request.setAttribute("allavions", avions);
                request.getRequestDispatcher("/WEB-INF/admin/ajout_vol.jsp").forward(request, response);
    }


// insertion vol 
  private void insererVol(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException {
        
        request.setAttribute("allaeroports", Aeroport.findAll());
        request.setAttribute("allavions", Avion.findAll());
        
        try {
            String numeroVol = request.getParameter("numeroVol");
            if (numeroVol == null || numeroVol.trim().isEmpty()) {
                throw new IllegalArgumentException("Le numéro de vol est obligatoire");
            }

            String statut = request.getParameter("statut");
            String porteEmbarquement = request.getParameter("porteEmbarquement");
            
            int idAeroportDepart = Integer.parseInt(request.getParameter("idAeroportDepart"));
            int idAeroportArrivee = Integer.parseInt(request.getParameter("idAeroportArrivee"));
            int idAvion = Integer.parseInt(request.getParameter("idAvion"));
            int equipage = Integer.parseInt(request.getParameter("equipage"));

            
            // // Validation des aéroports
            // if (idAeroportDepart == idAeroportArrivee) {
            //     throw new IllegalArgumentException("L'aéroport de départ et d'arrivée doivent être différents");
            // }

            // Conversion des dates
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime dateHeureDepart = LocalDateTime.parse(
                request.getParameter("dateHeureDepart") + ":00", formatter);
            LocalDateTime dateHeureArrivee = LocalDateTime.parse(
                request.getParameter("dateHeureArrivee") + ":00", formatter);
            
            // // Validation des dates
            // if (dateHeureDepart.isAfter(dateHeureArrivee)) {
            //     throw new IllegalArgumentException("La date de départ doit être avant la date d'arrivée");
            // }

            // if (dateHeureDepart.isBefore(LocalDateTime.now())) {
            //     throw new IllegalArgumentException("La date de départ ne peut pas être dans le passé");
            // }

            // Insertion du vol
            Vol.insert(idAvion, idAeroportDepart, idAeroportArrivee,
                    dateHeureDepart, dateHeureArrivee, numeroVol, statut, porteEmbarquement,equipage);

            // Redirection avec message de succès
            request.getSession().setAttribute("successMessage", "Vol ajouté avec succès");
            response.sendRedirect(request.getContextPath() + "/vols");

        } catch (Exception e) {
            // Réaffectation des valeurs du formulaire
            request.setAttribute("numeroVol", request.getParameter("numeroVol"));
            request.setAttribute("statut", request.getParameter("statut"));
            request.setAttribute("porteEmbarquement", request.getParameter("porteEmbarquement"));
            request.setAttribute("idAeroportDepart", request.getParameter("idAeroportDepart"));
            request.setAttribute("idAeroportArrivee", request.getParameter("idAeroportArrivee"));
            request.setAttribute("idAvion", request.getParameter("idAvion"));
            request.setAttribute("dateHeureDepart", request.getParameter("dateHeureDepart"));
            request.setAttribute("dateHeureArrivee", request.getParameter("dateHeureArrivee"));
            
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/admin/reponse.jsp").forward(request, response);
        }
    }

// formulaire modification vol
  protected void afficherFormulaireModification(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException {
        
    try {
        // 1. Récupération du paramètre avec le bon nom ("idVol" au lieu de "id")
        String idParam = request.getParameter("idVol");
        
        // 2. Validation du paramètre
        if (idParam == null || idParam.trim().isEmpty()) {
            throw new ServletException("Paramètre idVol manquant dans l'URL");
        }
        
        // 3. Conversion sécurisée
        int idVol;
        try {
            idVol = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            throw new ServletException("Format d'ID invalide. L'ID doit être un nombre.", e);
        }
        
        // 4. Récupération du vol
        Vol vol = Vol.findById(idVol);
        if (vol == null) {
            throw new ServletException("Aucun vol trouvé avec l'ID: " + idVol);
        }
        
        // 5. Préparation des données pour la vue
        request.setAttribute("vol", vol);
        request.setAttribute("allaeroports", Aeroport.findAll());
        request.setAttribute("allavions", Avion.findAll());
        
        request.getRequestDispatcher("/WEB-INF/admin/modifie_vol.jsp").forward(request, response);
        
    } catch (Exception e) {
        // Journalisation de l'erreur
        e.printStackTrace();
        // Redirection avec message d'erreur
        response.sendRedirect(request.getContextPath() + "/vols?error=" + 
            URLEncoder.encode("Erreur lors de l'accès au formulaire: " + e.getMessage(), "UTF-8"));
    }
}


// update vol
   private void updatevol(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        try {
            // Validation de l'ID
            String idVolParam = request.getParameter("idVol");
            if (idVolParam == null || idVolParam.isEmpty()) {
                throw new IllegalArgumentException("ID du vol manquant");
            }
            int idVol = Integer.parseInt(idVolParam);
            
            // Vérification que le vol existe
            Vol volExistant = Vol.findById(idVol);
            if (volExistant == null) {
                throw new IllegalArgumentException("Le vol avec l'ID " + idVol + " n'existe pas");
            }
            
            // Récupération des autres paramètres
            String numeroVol = request.getParameter("numeroVol");
            if (numeroVol == null || numeroVol.trim().isEmpty()) {
                throw new IllegalArgumentException("Le numéro de vol est obligatoire");
            }
            
            // Conversion des dates
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime dateHeureDepart = LocalDateTime.parse(
                request.getParameter("dateHeureDepart") + ":00", formatter);
            LocalDateTime dateHeureArrivee = LocalDateTime.parse(
                request.getParameter("dateHeureArrivee") + ":00", formatter);
            
            // Validation des dates
            if (dateHeureDepart.isAfter(dateHeureArrivee)) {
                throw new IllegalArgumentException("La date de départ doit être avant la date d'arrivée");
            }
            
            // Création de l'objet Vol modifié
            Vol volModifie = new Vol();
            volModifie.setIdVol(idVol);
            volModifie.setNumeroVol(numeroVol);
            volModifie.setIdAvion(Integer.parseInt(request.getParameter("idAvion")));
            volModifie.setIdAeroportDepart(Integer.parseInt(request.getParameter("idAeroportDepart")));
            volModifie.setIdAeroportArrivee(Integer.parseInt(request.getParameter("idAeroportArrivee")));
            volModifie.setDateHeureDepart(dateHeureDepart);
            volModifie.setDateHeureArrivee(dateHeureArrivee);
            volModifie.setStatut(request.getParameter("statut"));
            volModifie.setPorteEmbarquement(request.getParameter("porteEmbarquement"));
            volModifie.setEquipage(Integer.parseInt(request.getParameter("equipage")));
            
            // Mise à jour dans la base de données
            Vol.update(volModifie);
            
            // Redirection avec message de succès
            response.sendRedirect(request.getContextPath() + "/vols?success=Le+vol+a+été+modifié+avec+succès");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("/WEB-INF/admin/modifie_vol.jsp").forward(request, response);
        } 
    }

// suprimer vol
        private void supprimerVol(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
            try {
                // 1. Récupération de l'ID du vol à supprimer
                int idVol = Integer.parseInt(request.getParameter("id"));

                // 2. Vérification de l'existence du vol (optionnel)
                Vol volASupprimer = Vol.findById(idVol);
                if (volASupprimer == null) {
                    throw new Exception("Le vol à supprimer n'existe pas");
                }

                // 3. Suppression du vol
                Vol.delete(idVol);

                // 4. Redirection avec message de succès
                response.sendRedirect(request.getContextPath() + "/vols?success=Le vol a été supprimé avec succès");

            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/vols?error=ID de vol invalide");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/vols?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
            }
            
        }


// rechercher vol
       protected void searchVols(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String keyword = request.getParameter("keyword");
            
            List<Vol> vols = new ArrayList<>();
            if (keyword != null && !keyword.trim().isEmpty()) {
                vols = Vol.search(keyword.trim());
            }
            
            request.setAttribute("vols", vols);
            request.setAttribute("keyword", keyword);
            request.getRequestDispatcher("/WEB-INF/admin/index.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Erreur lors de la recherche: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/admin/index.jsp").forward(request, response);
        }
    }



// rechercher reservatioon
   protected void searchReservations(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    try {
        String keyword = request.getParameter("keyword");
        List<Reservation> reservations = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            reservations = Reservation.searchReservations(keyword.trim());
        } else {
            reservations = Reservation.findAll();
        }
        
        request.setAttribute("reservations", reservations);
        request.setAttribute("keyword", keyword);
        
        if (!response.isCommitted()) {
            request.getRequestDispatcher("/WEB-INF/admin/liste_reservation.jsp").forward(request, response);
        }
        
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("erreur", "Erreur lors de la recherche: " + e.getMessage());
        
        if (!response.isCommitted()) {
            request.getRequestDispatcher("/WEB-INF/admin/liste_reservation.jsp").forward(request, response);
        }
    }
}
    

   private void affichierreservation(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    try {
        List<Reservation> reservations = Reservation.findAll();
        request.setAttribute("allreservations", reservations);
        
        if (!response.isCommitted()) {
            request.getRequestDispatcher("/WEB-INF/admin/liste_reservation.jsp").forward(request, response);
        }
        
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("error", "Erreur lors de la récupération des réservations");
        
        if (!response.isCommitted()) {
            request.getRequestDispatcher("/WEB-INF/admin/liste_reservation.jsp").forward(request, response);
        }
    }
}


    protected void searchByAirports(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    try {
        int idAeroportDepart = Integer.parseInt(request.getParameter("idAeroportDepart"));
        int idAeroportArrivee = Integer.parseInt(request.getParameter("idAeroportArrivee"));
        
        List<Vol> vols = Vol.searchByAirports(idAeroportDepart, idAeroportArrivee);
        request.setAttribute("volsbyaeroport", vols);
        
        if (!response.isCommitted()) {
            request.getRequestDispatcher("/WEB-INF/client/index.jsp").forward(request, response);
        }
        
    } catch (Exception e) {
        request.setAttribute("erreur", "Erreur lors de la recherche: " + e.getMessage());
        
        if (!response.isCommitted()) {
            request.getRequestDispatcher("/WEB-INF/client/index.jsp").forward(request, response);
        }
    }
}


public void afficherPrix(HttpServletRequest request, HttpServletResponse response) {
        int idClasseVol = Integer.parseInt(request.getParameter("idClasseVol"));
        
        try {
            ClassVol classe = ClassVol.findById(idClasseVol);
            classe.mettreAJourPrixSiDelaisDepasse(); // Mise à jour si nécessaire
            
            request.setAttribute("prixAdulte", classe.getPrixAdulte());
            request.setAttribute("prixEnfant", classe.getPrixEnfant());
            request.getRequestDispatcher("/afficherPrix.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer l'erreur
        }
    }


}

