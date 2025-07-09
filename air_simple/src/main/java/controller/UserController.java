package controller;

import java.io.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

import model.Vol;
import model.VueVol;
import model.Aeroport;
import model.ClassVol;
import model.Reservation;

public class UserController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getServletPath();
        
        try {
            switch(action) {
                case "/client":
                    versindex(request, response);
                    break;
                case "/ReserverVol":
                    afficherFormulaireReservation(request, response);
                    break;
                case "/getreservation":
                    afficherlistereservation(request, response);
                    break;
                case "/liste_reservation_admin":
                    afficherlistereservation_admin(request, response);
                    break;
                case "/annulerReserverVol":
                    annulerReservation(request, response);
                    break;
                case "/payerReservation":
                    payerReservation(request, response);
                    break;

                default:
                    if (!response.isCommitted()) {
                        response.sendError(404);
                    }
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getServletPath();
        
        try {
            if ("/CreerReservation".equals(action)) {
                creerReservation(request, response);
            }else if("/reserver".equals(action)){
                    creerReservation(request, response);
            }else if (!response.isCommitted()) {
                    response.sendError(404);
                
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }


  private void versindex(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<VueVol> vols = VueVol.findAll();
        List<Aeroport> aeroports = Aeroport.findAll();
        
        request.setAttribute("allaeroports", aeroports);
        request.setAttribute("vuevols", vols);
        
        if (!response.isCommitted()) {
            request.getRequestDispatcher("/WEB-INF/client/index.jsp").forward(request, response);
        }
    }


    private void afficherlistereservation(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Reservation> reservations = Reservation.findAll();
        request.setAttribute("allresa", reservations);
        request.getRequestDispatcher("/WEB-INF/client/reservationlist.jsp").forward(request, response);
    }

    private void afficherlistereservation_admin(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Reservation> reservations = Reservation.findAll();
        request.setAttribute("allresa", reservations);
        request.getRequestDispatcher("/WEB-INF/admin/liste_reservation.jsp").forward(request, response);
    }

      

  protected void afficherFormulaireReservation(HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println("=== DEBUT METHODE ===");
            
            // 1. Validation paramètre
            String idVolParam = request.getParameter("idVol");
            System.out.println("Paramètre idVol reçu: " + idVolParam);
            
            if (idVolParam == null || idVolParam.trim().isEmpty()) {
                System.out.println("Erreur: idVol manquant");
                request.setAttribute("errorMessage", "L'identifiant du vol est requis");
                request.getRequestDispatcher("/WEB-INF/client/reservation.jsp").forward(request, response);
                return;
            }

            // 2. Conversion ID
            int idVol;
            try {
                idVol = Integer.parseInt(idVolParam);
                System.out.println("ID Vol converti: " + idVol);
            } catch (NumberFormatException e) {
                System.out.println("Erreur conversion: " + e.toString());
                request.setAttribute("errorMessage", "ID de vol invalide");
                request.getRequestDispatcher("/WEB-INF/client/reservation.jsp").forward(request, response);
                return;
            }

            // 3. Récupération vol
            System.out.println("Recherche du vol...");
            Vol vol = Vol.findById(idVol);
            System.out.println("Vol trouvé: " + (vol != null ? vol.toString() : "null"));

            if (vol == null) {
                request.setAttribute("errorMessage", "Aucun vol trouvé avec l'ID: " + idVol);
                request.getRequestDispatcher("/WEB-INF/client/reservation.jsp").forward(request, response);
                return;
            }

            // 4. Récupération classes
            System.out.println("Recherche des classes...");
            List<ClassVol> classesVol = ClassVol.findByVolId(idVol);
            System.out.println("Classes trouvées: " + (classesVol != null ? classesVol.size() : "null"));

            if (classesVol == null || classesVol.isEmpty()) {
                request.setAttribute("errorMessage", "Aucune classe disponible");
                request.getRequestDispatcher("/WEB-INF/client/reservation.jsp").forward(request, response);
                return;
            }

            // 5. Préparation attributs
            request.setAttribute("vol", vol);
            request.setAttribute("classesVol", classesVol);

            // 6. Forward
            System.out.println("Forward vers la vue");
            request.getRequestDispatcher("/WEB-INF/client/reservation.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println("ERREUR GLOBALE: " + e.toString());
            e.printStackTrace();
            try {
                request.setAttribute("errorMessage", "Erreur système: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/erreur.jsp").forward(request, response);
            } catch (Exception ex) {
                System.out.println("ERREUR LORS DE L'AFFICHAGE DE L'ERREUR: " + ex.toString());
            }
        }
    }



   protected void creerReservation(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
        try {
            // 1. Récupérer les paramètres
            int idVol = Integer.parseInt(request.getParameter("idVol"));
            List<ClassVol> classesVol = ClassVol.findByVolId(idVol);
            
            // 2. Valider qu'il y a des classes
            if (classesVol == null || classesVol.isEmpty()) {
                throw new ServletException("Aucune classe disponible pour ce vol");
            }
            
            // 3. Traiter chaque classe
            for (ClassVol classe : classesVol) {
                int adultes = Integer.parseInt(request.getParameter("adultes_" + classe.getIdClasse()));
                int enfants = Integer.parseInt(request.getParameter("enfants_" + classe.getIdClasse()));
                String statut = "non paye";
                
                // 4. Ignorer si aucun passager
                if (adultes + enfants == 0) {
                    continue;
                }
                
                // 5. Vérifier les places disponibles
                if (classe.getPlacesDisponibles() < (adultes + enfants)) {
                    throw new ServletException("Pas assez de places disponibles en " + classe.getNomClasse());
                }
                
                // 6. Créer la réservation
                Reservation reservation = new Reservation(
                    idVol, 
                    classe.getIdClasseVol(),
                    adultes,
                    enfants,
                    statut
                );
                
                // 7. Sauvegarder
                Reservation.insert(reservation);
                
                // 8. Mettre à jour les places disponibles
                classe.setPlacesDisponibles(classe.getPlacesDisponibles() - (adultes + enfants));
                ClassVol.update(classe);
                
            }
            
            // 9. Recharger le formulaire avec message de succès
            request.setAttribute("successMessage", "Réservation(s) enregistrée(s) avec succès");
            response.sendRedirect(request.getContextPath() + "/getreservation");
            
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Erreur de réservation: " + e.getMessage());
            afficherFormulaireReservation(request, response);
        }
    }
    
    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {
        e.printStackTrace();
        request.setAttribute("errorMessage", "Erreur: " + e.getMessage());
        
        if (!response.isCommitted()) {
        }
    }


   protected void annulerReservation(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException,Exception {
    
    try {
        int idReservation = Integer.parseInt(request.getParameter("id"));
        
        // 1. Récupérer la réservation
        Reservation reservation = Reservation.getReservationWithDetails(idReservation);
        if (reservation == null) {
            throw new ServletException("Réservation introuvable");
        }
        
        // 2. Vérifier si déjà annulée
        if (Reservation.STATUT_ANNULE.equals(reservation.getStatut())) {
            request.setAttribute("warningMessage", "Cette réservation est déjà annulée");
        } else {
            // 3. Annuler la réservation
            boolean success = Reservation.annulerReservation(idReservation);
            
            if (success) {
                // 4. Mettre à jour les places disponibles si nécessaire
                ClassVol classe = ClassVol.findById(reservation.getIdClasseVol());
                if (classe != null) {
                    int nouvellesPlaces = classe.getPlacesDisponibles() + 
                                        reservation.getTouslespersonnes();
                    classe.setPlacesDisponibles(nouvellesPlaces);
                    ClassVol.update(classe);
                }
                
                request.setAttribute("successMessage", "Réservation annulée avec succès");
            } else {
                throw new ServletException("Échec de l'annulation");
            }
        }
        
    } catch (Exception e) {
        request.setAttribute("errorMessage", "Erreur lors de l'annulation: " + e.getMessage());
    }
    
    // Recharger la liste des réservations
    afficherlistereservation(request, response);
}


protected void payerReservation(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException,Exception {
    
    try {
        int idReservation = Integer.parseInt(request.getParameter("id"));
        
        // 1. Payer la réservation
        boolean success = Reservation.payerReservation(idReservation);
        
        if (success) {
            request.setAttribute("successMessage", "Paiement confirmé avec succès");
        } else {
            request.setAttribute("errorMessage", "Échec du paiement - Réservation déjà payée ou annulée");
        }
        
    } catch (Exception e) {
        request.setAttribute("errorMessage", "Erreur lors du paiement: " + e.getMessage());
    }
    
    // Recharger la liste des réservations
    afficherlistereservation(request, response);
}


}