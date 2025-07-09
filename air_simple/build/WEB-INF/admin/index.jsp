<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.VueVol" %>
<%@ page import="model.Vol" %>

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
                    <li><a href="compagnies"><i class="fas fa-building"></i> Compagnies</a></li>
                    <li><a href="aeroports"><i class="fas fa-map-marker-alt"></i> Aéroports</a></li>
                    <li class="dropdown">
                        <a href="#"><i class="fas fa-user-circle"></i> Admin <i class="fas fa-caret-down"></i></a>
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
                    <input type="text" id="keyword" name="keyword" 
                        value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : "" %>" 
                        placeholder="Numéro de vol, statut, porte...">
                    <button type="submit">Rechercher</button>
                    <a href="vols" class="reset-btn">Réinitialiser</a>
                </div>
            </form>
        </div>

        <%
            List<Vol> vols = (List<Vol>) request.getAttribute("vols");
            String keyword = request.getParameter("keyword");
            
            if (vols != null && !vols.isEmpty()) {
        %>
            <h3>Résultats pour "<%= keyword %>"</h3>
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
                                <a href="ModifierVol?id=<%= vol.getIdVol() %>">Modifier</a>
                                <a href="SupprimerVol?id=<%= vol.getIdVol() %>" 
                                onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce vol?')">Supprimer</a>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <%
            } else if (keyword != null && !keyword.isEmpty()) {
        %>
            <p class="no-results">Aucun vol trouvé pour "<%= keyword %>"</p>
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
                            <th>Classe</th>
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
                                    <button class="btn btn-edit" onclick="window.location.href='class?idVol=<%= vol.getIdVol() %>'">Classer</button>
                                </td>
                                 <td class="action-buttons">
                                    <button class="btn btn-edit" onclick="window.location.href='ModifierVol?idVol=<%= vol.getIdVol() %>'">Modifier</button>
                                    <button class="btn btn-delete" onclick="if(confirm('Êtes-vous sûr de vouloir supprimer ce vol?')) window.location.href='SupprimerVol?id=<%= vol.getIdVol() %>'">Supprimer</button>
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