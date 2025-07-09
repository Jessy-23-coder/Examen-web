<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.VueVol" %>
<%@ page import="model.Vol" %>
<%@ page import="model.Aeroport" %>

<%
    List<Aeroport> aeroports = (List<Aeroport>) request.getAttribute("allaeroports");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
     <link rel="stylesheet" href="css/index.css">
    <title>Fly High</title>
</head>
<body>

    <nav class="navbar">
        <div class="navbar-container">
            <div class="navbar-brand">
                <a href="vols">
                    <img src="img/logo.png" alt="Logo Compagnie" class="logo">
                </a>
            </div>
            
            <button class="navbar-toggler" aria-label="Menu">
                <span></span>
                <span></span>
                <span></span>
            </button>
            
            <div class="navbar-links">
                <ul>
                    <li><a href="vols" class="active"><i class="fas fa-plane"></i> Liste des Vols</a></li>
                    <li><a href="ajoute_vol"><i class="fas fa-plus-circle"></i> Ajouter Vol</a></li>
                    <li><a href="reservation"><i class="fas fa-building"></i> Compagnies</a></li>
                    <li class="dropdown">
                        <a href="vols"><i class="fas fa-user-circle"></i> Admin <i class="fas fa-caret-down"></i></a>
                        <div class="dropdown-content">
                            <a href="parametres"><i class="fas fa-cog"></i> Paramètres</a>
                            <a href="deconnexion"><i class="fas fa-sign-out-alt"></i> Déconnexion</a>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </nav>


    <div class="container">
        <div class="content">
            <h2>Liste des vols</h2>

        <div class="search-container">
            <h2>Rechercher un vol</h2>
            <form method="GET" action="GestionVols">
                <div class="form-group-line">
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
                    <button type="submit">Rechercher</button>
                    <a href="vols" class="reset-btn">Réinitialiser</a>
                </div>
            </form>
        </div>

        <%
            List<Vol> vols = (List<Vol>) request.getAttribute("volsbyaeroport");
            
            if (vols != null && !vols.isEmpty()) {
        %>
            <h3>Résultats</h3>
            <table>
                <thead>
                    <tr>
                        <th>Numéro de vol</th>
                        <th>Départ</th>
                        <th>Arrivée</th>
                        <th>Statut</th>
                        <th>Porte</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Vol vol : vols) { %>
                        <tr>
                            <td><%= vol.getNumeroVol() %></td>
                            <td>
                                <%= vol.getDateHeureDepart() %>
                            </td>
                            <td>
                                <%= vol.getDateHeureArrivee() %>
                            </td>
                            <td><%= vol.getStatut() %></td>
                            <td><%= vol.getPorteEmbarquement() %></td>
                            <td>
                                <button class="btn btn-edit" onclick="window.location.href='ReserverVol?idVol=<%= vol.getIdVol() %>'">Reserver</button>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <%
            } else  {
        %>
            <p class="no-results">Aucun vol trouvé </p>
        <%
            }
        %>

        <!-- Bouton d'ajout -->
        <button class="btn btn-add" onclick="window.location.href='ajout_vol'">Ajouter un nouveau vol</button>
        
            <%-- Affichage des erreurs --%>


            <% 
                String error = (String) request.getAttribute("error");
                if (error != null) { 
            %>
                <div style="color:red; padding:10px; margin:10px 0; border:1px solid red;">
                    <%= error %>
                </div>
            <% } %>
            
            <%-- Tableau des vols --%>
            <%
                List<VueVol> vuevols = (List<VueVol>) request.getAttribute("vuevols");
                if (vuevols != null && !vuevols.isEmpty()) { 
            %>
                <table>
                    <thead>
                        <tr>
                            <th>Numéro</th>
                            <th>Compagnie</th>
                            <th>Avion</th>
                            <th>Départ</th>
                            <th>Arrivée</th>
                            <th>Date/Heure</th>
                            <th>Statut</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (VueVol vol : vuevols) { %>
                            <tr>
                                <td><%= vol.getNumeroVol() %></td>
                                <td>
                                    <%= vol.getCompagnieAerienne() %><br>
                                    <small><%= vol.getCodeCompagnie() %></small>
                                </td>
                                 <td>
                                    <%= vol.getAvionModele() %><br>
                                    <small><%= vol.getAvionModele() %></small>
                                </td>
                                <td>
                                    <%= vol.getAeroportDepart() %><br>
                                    <small><%= vol.getCodeDepart() %></small>
                                </td>
                                <td>
                                    <%= vol.getAeroportArrivee() %><br>
                                    <small><%= vol.getCodeArrivee() %></small>
                                </td>
                                <td><%= vol.getDateHeureDepart() %></td>
                                <td>
                                    <% 
                                        String statut = vol.getStatut();
                                        String color = "black";
                                        if ("à l heure".equals(statut)) color = "green";
                                        else if ("retardé".equals(statut)) color = "orange";
                                        else if ("annulé".equals(statut)) color = "red";
                                    %>
                                    <span style="color:<%= color %>;">
                                        <%= statut %>
                                    </span>
                                </td>
                                 <td class="action-buttons">
                                    <button class="btn btn-edit" onclick="window.location.href='ReserverVol?idVol=<%= vol.getIdVol() %>'">Reserver</button>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <p style="text-align:center; margin:20px 0;">Aucun vol disponible</p>
            <% } %>
        </div>
    </div>

    <script>
    // Script pour le menu mobile
    document.querySelector('.navbar-toggler').addEventListener('click', function() {
        document.querySelector('.navbar-links').classList.toggle('active');
        
        // Animation de l'icône hamburger
        this.classList.toggle('open');
    });
</script>
</body>
</html> 