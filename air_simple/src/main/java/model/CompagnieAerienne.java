package model;

import java.time.LocalDate;

public class CompagnieAerienne {
    private int idCompagnie;
    private String nom;
    private String codeIATA;
    private String logoPath; 
    private String description;
    private LocalDate dateCreation;

    public int getIdCompagnie() {
        return idCompagnie;
    }
    public void setIdCompagnie(int idCompagnie) {
        this.idCompagnie = idCompagnie;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getCodeIATA() {
        return codeIATA;
    }
    public void setCodeIATA(String codeIATA) {
        this.codeIATA = codeIATA;
    }
    public String getLogoPath() {
        return logoPath;
    }
    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDate getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    
}
