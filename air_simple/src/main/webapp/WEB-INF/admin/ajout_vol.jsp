<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="model.Aeroport" %>
<%@ page import="model.Avion" %>

<%
    List<Aeroport> aeroports = (List<Aeroport>) request.getAttribute("allaeroports");
    List<Avion> avions = (List<Avion>) request.getAttribute("allavions");
    String nowDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).substring(0, 16);
%>

<html>
<head>
    <title>Ajout d'un nouveau vol</title>
    <link rel="stylesheet" href="css/ajout_vol.css">
</head>
<body>
    <div class="content">
        <h1>Ajouter un nouveau vol</h1>
        
        <form method="POST" action="insert_vol">
            <div class="form-group-line">
                <label for="numeroVol">Numéro de vol</label>
                <input type="text" id="numeroVol" name="numeroVol" required>
            </div>
            
            <div class="form-group-line">
                <label for="idAvion">Avion</label>
                <select id="idAvion" name="idAvion" required>
                    <option value="">-- Sélectionnez --</option>
                    <% if (avions != null && !avions.isEmpty()) { 
                        for (Avion avion : avions) { %>
                            <option value="<%= avion.getIdAvion() %>"><%= avion.getModele() %></option>
                        <% } 
                    } else { %>
                        <option value="" disabled>Aucun avion disponible</option>
                    <% } %>
                </select>
            </div>

            <div class="form-group-line">
                <label for="idAeroportDepart">Aéroport de départ</label>
                <select id="idAeroportDepart" name="idAeroportDepart" required>
                    <option value="">-- Sélectionnez --</option>
                    <% if (aeroports != null && !aeroports.isEmpty()) { 
                        for (Aeroport aeroport : aeroports) { %>
                            <option value="<%= aeroport.getIdAeroport() %>"><%= aeroport.getNom() %></option>
                        <% } 
                    } else { %>
                        <option value="" disabled>Aucun aéroport disponible</option>
                    <% } %>
                </select>
            </div>

            <div class="form-group-line">
                <label for="idAeroportArrivee">Aéroport d'arrivée</label>
                <select id="idAeroportArrivee" name="idAeroportArrivee" required>
                    <option value="">-- Sélectionnez --</option>
                    <% if (aeroports != null && !aeroports.isEmpty()) { 
                        for (Aeroport aeroport : aeroports) { %>
                            <option value="<%= aeroport.getIdAeroport() %>"><%= aeroport.getNom() %></option>
                        <% } 
                    } else { %>
                        <option value="" disabled>Aucun aéroport disponible</option>
                    <% } %>
                </select>
            </div>
            
            <div class="form-group-line">
                <label for="dateHeureDepart">Date/Heure Départ</label>
                <input type="datetime-local" id="dateHeureDepart" name="dateHeureDepart" required
                    step="60" min="<%= nowDateTime.substring(0, 16) %>">  <!-- step="60" pour des minutes entières -->
            </div>

            <div class="form-group-line">
                <label for="dateHeureArrivee">Date/Heure Arrivée</label>
                <input type="datetime-local" id="dateHeureArrivee" name="dateHeureArrivee" required
                    step="60" min="<%= nowDateTime.substring(0, 16) %>">
            </div>
            
            <div class="form-group-line">
                <label for="statut">Statut:</label>
                <select id="statut" name="statut" required>
                    <option value="">-- Sélectionnez --</option>
                    <option value="à l'heure">à l'heure</option>
                    <option value="retardé">Retardé</option>
                    <option value="annulé">Annulé</option>
                    <option value="embarquement">Embarquement</option>
                    <option value="décollé">Décollé</option>
                    <option value="atterri">Atterri</option>
                </select>
            </div>
            
            <div class="form-group-line">
                <label for="porteEmbarquement">Porte d'embarquement</label>
                <input type="text" id="porteEmbarquement" name="porteEmbarquement">
            </div>

            <div class="form-group-line">
                <label for="equipage">Equipage</label>
                <input type="text" id="equipage" name="equipage">
            </div>
            
            <div class="form-actions">
                <button type="submit">Ajouter le vol</button>
                <a href="vols" class="cancel-btn">Retour à la liste</a>
            </div>
        </form>
    </div>
</body>
</html>