package controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import model.ClassVol;

import java.sql.SQLException;

@WebListener
public class PrixInitializer implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Initialiser les paramètres d'augmentation pour tous les vols
            ClassVol.initialiserAugmentationPrix(0);
            System.out.println("Configuration d'augmentation des prix initialisée");
            
            // Optionnel : planifier une vérification périodique
            planifierVerificationPrix();
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation des prix: " + e.getMessage());
        }
    }

    private void planifierVerificationPrix() {
        // Implémentation de la vérification périodique
        // (voir point 3 ci-dessous)
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Nettoyage si nécessaire
    }
}