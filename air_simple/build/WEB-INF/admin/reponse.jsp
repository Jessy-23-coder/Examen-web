<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Aeroport" %>
<%@ page import="model.Avion" %>

<%
    List<Aeroport> aeroports = (List<Aeroport>) request.getAttribute("allaeroports");
    List<Avion> avions = (List<Avion>) request.getAttribute("allavions");
    String erreur = (String) request.getAttribute("erreur");
%>

<html>
<head>
    <title>Erreur d'insertion</title>
    <link rel="stylesheet" href="css/ajout_vol.css">
</head>
<body>
    <div class="content">
        <h1 class="error">Erreur lors de l'ajout du vol</h1>
        
        <% if (erreur != null) { %>
            <div class="error-message"><%= erreur %></div>
        <% } %>
        
        <form method="POST" action="insert_vol">
            <!-- Champ Numéro de vol -->
            <div class="form-group-line">
                <label for="numeroVol">Numéro de vol</label>
                <input type="text" id="numeroVol" name="numeroVol" 
                       value="<%= request.getAttribute("numeroVol") != null ? request.getAttribute("numeroVol") : "" %>" required>
            </div>
            
            <!-- Champ Avion -->
            <div class="form-group-line">
                <label for="idAvion">Avion</label>
                <select id="idAvion" name="idAvion" required>
                    <option value="">-- Sélectionnez --</option>
                    <% if (avions != null) { 
                        String selectedAvion = request.getAttribute("idAvion") != null ? request.getAttribute("idAvion").toString() : "";
                        for (Avion avion : avions) { 
                            boolean selected = String.valueOf(avion.getIdAvion()).equals(selectedAvion);
                    %>
                            <option value="<%= avion.getIdAvion() %>" <%= selected ? "selected" : "" %>>
                                <%= avion.getModele() %>
                            </option>
                    <% } } %>
                </select>
            </div>

            <!-- Champ Aéroport de départ -->
            <div class="form-group-line">
                <label for="idAeroportDepart">Aéroport de départ</label>
                <select id="idAeroportDepart" name="idAeroportDepart" required>
                    <option value="">-- Sélectionnez --</option>
                    <% if (aeroports != null) { 
                        String selectedDepart = request.getAttribute("idAeroportDepart") != null ? request.getAttribute("idAeroportDepart").toString() : "";
                        for (Aeroport aeroport : aeroports) { 
                            boolean selected = String.valueOf(aeroport.getIdAeroport()).equals(selectedDepart);
                    %>
                            <option value="<%= aeroport.getIdAeroport() %>" <%= selected ? "selected" : "" %>>
                                <%= aeroport.getNom() %>
                            </option>
                    <% } } %>
                </select>
            </div>

            <!-- Champ Aéroport d'arrivée -->
            <div class="form-group-line">
                <label for="idAeroportArrivee">Aéroport d'arrivée</label>
                <select id="idAeroportArrivee" name="idAeroportArrivee" required>
                    <option value="">-- Sélectionnez --</option>
                    <% if (aeroports != null) { 
                        String selectedArrivee = request.getAttribute("idAeroportArrivee") != null ? request.getAttribute("idAeroportArrivee").toString() : "";
                        for (Aeroport aeroport : aeroports) { 
                            boolean selected = String.valueOf(aeroport.getIdAeroport()).equals(selectedArrivee);
                    %>
                            <option value="<%= aeroport.getIdAeroport() %>" <%= selected ? "selected" : "" %>>
                                <%= aeroport.getNom() %>
                            </option>
                    <% } } %>
                </select>
            </div>
            
            <!-- Champ Date/Heure Départ -->
            <div class="form-group-line">
                <label for="dateHeureDepart">Date/Heure Départ</label>
                <input type="datetime-local" id="dateHeureDepart" name="dateHeureDepart" 
                       value="<%= request.getAttribute("dateHeureDepart") != null ? request.getAttribute("dateHeureDepart") : "" %>" required>
            </div>
            
            <!-- Champ Date/Heure Arrivée -->
            <div class="form-group-line">
                <label for="dateHeureArrivee">Date/Heure Arrivée</label>
                <input type="datetime-local" id="dateHeureArrivee" name="dateHeureArrivee" 
                       value="<%= request.getAttribute("dateHeureArrivee") != null ? request.getAttribute("dateHeureArrivee") : "" %>" required>
            </div>
            
            <!-- Champ Statut -->
            <div class="form-group-line">
                <label for="statut">Statut:</label>
                <select id="statut" name="statut" required>
                    <option value="">-- Sélectionnez --</option>
                    <% 
                    String[] statuts = {"à l heure", "retardé", "annulé", "embarquement", "décollé", "atterri"};
                    String selectedStatut = request.getAttribute("statut") != null ? request.getAttribute("statut").toString() : "";
                    for (String s : statuts) { 
                        boolean selected = s.equals(selectedStatut);
                    %>
                        <option value="<%= s %>" <%= selected ? "selected" : "" %>>
                            <%= s.replace("à l heure", "à l'heure") %>
                        </option>
                    <% } %>
                </select>
            </div>
            
            <!-- Champ Porte d'embarquement -->
            <div class="form-group-line">
                <label for="porteEmbarquement">Porte d'embarquement</label>
                <input type="text" id="porteEmbarquement" name="porteEmbarquement"
                       value="<%= request.getAttribute("porteEmbarquement") != null ? request.getAttribute("porteEmbarquement") : "" %>">
            </div>
            
            <button type="submit">Réessayer l'ajout</button>
            <a href="vols">Retour à la liste</a>
        </form>
    </div>
</body>
</html>