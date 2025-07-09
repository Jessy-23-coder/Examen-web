<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Aeroport" %>
<%@ page import="model.Avion" %>
<%@ page import="model.Vol" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    List<Aeroport> aeroports = (List<Aeroport>) request.getAttribute("allaeroports");
    List<Avion> avions = (List<Avion>) request.getAttribute("allavions");
    Vol vol = (Vol) request.getAttribute("vol");
    
    // Formatage des dates pour l'input datetime-local
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    String dateDepartFormatted = "";
    String dateArriveeFormatted = "";
    
    if (vol != null) {
        dateDepartFormatted = vol.getDateHeureDepart().format(formatter);
        dateArriveeFormatted = vol.getDateHeureArrivee().format(formatter);
    }
%>

<html>
<head>
    <title>Modification d'un vol</title>
    <link rel="stylesheet" href="css/ajout_vol.css">
</head>
<body>
    <div class="content">
        <h1>Modifier le vol <%= vol != null ? vol.getNumeroVol() : "" %></h1>
        
        <form method="POST" action="update_vol">
            <!-- Champ caché pour l'ID -->
             <div class="form-group-line">
                <label for="numeroVol">id de vol</label>
                <input  type="text" id="numeroVol" name="idVol" 
                        value="<%= vol != null ? vol.getIdVol() : "" %>">

            </div>
            
            <div class="form-group-line">
                <label for="numeroVol">Numéro de vol</label>
                <input type="text" id="numeroVol" name="numeroVol" 
                       value="<%= vol != null ? vol.getNumeroVol() : "" %>" required>
            </div>
            
            <div class="form-group-line">
                <label for="idAvion">Avion</label>
                <select id="idAvion" name="idAvion" required>
                    <option value="">-- Sélectionnez --</option>
                    <%
                    if (avions != null && !avions.isEmpty()) { 
                        for (Avion avion : avions) { 
                            boolean selected = (vol != null && avion.getIdAvion() == vol.getIdAvion());
                    %>
                            <option value="<%= avion.getIdAvion() %>" <%= selected ? "selected" : "" %>>
                                <%= avion.getModele() %>
                            </option>
                    <%  } 
                    } else { %>
                        <p style="text-align:center; margin:20px 0;">Aucun avion disponible</p>
                    <% } %>
                </select>
            </div>

            <div class="form-group-line">
                <label for="idAeroportDepart">Aéroport de départ</label>
                <select id="idAeroportDepart" name="idAeroportDepart" required>
                    <option value="">-- Sélectionnez --</option>
                    <%
                    if (aeroports != null && !aeroports.isEmpty()) { 
                        for (Aeroport aeroport : aeroports) { 
                            boolean selected = (vol != null && aeroport.getIdAeroport() == vol.getIdAeroportDepart());
                    %>
                            <option value="<%= aeroport.getIdAeroport() %>" <%= selected ? "selected" : "" %>>
                                <%= aeroport.getNom() %>
                            </option>
                    <%  } 
                    } else { %>
                        <p style="text-align:center; margin:20px 0;">Aucun aeroport disponible</p>
                    <% } %>
                </select>
            </div>

            <div class="form-group-line">
                <label for="idAeroportArrivee">Aéroport d'arrivée</label>
                <select id="idAeroportArrivee" name="idAeroportArrivee" required>
                    <option value="">-- Sélectionnez --</option>
                    <%
                    if (aeroports != null && !aeroports.isEmpty()) { 
                        for (Aeroport aeroport : aeroports) { 
                            boolean selected = (vol != null && aeroport.getIdAeroport() == vol.getIdAeroportArrivee());
                    %>
                            <option value="<%= aeroport.getIdAeroport() %>" <%= selected ? "selected" : "" %>>
                                <%= aeroport.getNom() %>
                            </option>
                    <%  } 
                    } else { %>
                        <p style="text-align:center; margin:20px 0;">Aucun aeroport disponible</p>
                    <% } %>
                </select>
            </div>
            
            <div class="form-group-line">
                <label for="dateHeureDepart">Date/Heure Départ</label>
                <input type="datetime-local" id="dateHeureDepart" name="dateHeureDepart" 
                       value="<%= dateDepartFormatted %>" required>
            </div>
            
            <div class="form-group-line">
                <label for="dateHeureArrivee">Date/Heure Arrivée</label>
                <input type="datetime-local" id="dateHeureArrivee" name="dateHeureArrivee" 
                       value="<%= dateArriveeFormatted %>" required>
            </div>
            
            <div class="form-group-line">
                <label for="statut">Statut:</label>
                <select id="statut" name="statut" required>
                    <option value="">-- Sélectionnez --</option>
                    <option value="à l heure" <%= vol != null && "à l heure".equals(vol.getStatut()) ? "selected" : "" %>>à l'heure</option>
                    <option value="retardé" <%= vol != null && "retardé".equals(vol.getStatut()) ? "selected" : "" %>>retardé</option>
                    <option value="annulé" <%= vol != null && "annulé".equals(vol.getStatut()) ? "selected" : "" %>>annulé</option>
                    <option value="embarquement" <%= vol != null && "embarquement".equals(vol.getStatut()) ? "selected" : "" %>>embarquement</option>
                    <option value="décollé" <%= vol != null && "décollé".equals(vol.getStatut()) ? "selected" : "" %>>décollé</option>
                    <option value="atterri" <%= vol != null && "atterri".equals(vol.getStatut()) ? "selected" : "" %>>atterri</option>
                </select>
            </div>
            
            <div class="form-group-line">
                <label for="porteEmbarquement">Porte d'embarquement</label>
                <input type="text" id="porteEmbarquement" name="porteEmbarquement" 
                       value="<%= vol != null ? vol.getPorteEmbarquement() : "" %>">
            </div>

            <div class="form-group-line">
                <label for="equipage">Equipage</label>
                <input type="text" id="equipage" name="equipage" 
                       value="<%= vol != null ? vol.getEquipage() : "" %>">
            </div>
            
            <button type="submit">Mettre à jour</button>
            <a href="vols">Retour à la liste</a>
        </form>
    </div>
</body>
</html>