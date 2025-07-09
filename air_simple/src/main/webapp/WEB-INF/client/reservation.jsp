<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.ClassVol" %>
<%@ page import="model.Vol" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Réservation de vol</title>
    <style>
        .error { color: red; }
        .container { width: 80%; margin: 0 auto; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 8px; border: 1px solid #ddd; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Réservation pour le vol <%= ((Vol)request.getAttribute("vol")).getIdVol() %></h1>
        
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="error"><%= request.getAttribute("errorMessage") %></div>
        <% } %>
        
        <% 
        List<ClassVol> classesVol = (List<ClassVol>) request.getAttribute("classesVol");
        if (classesVol == null || classesVol.isEmpty()) { 
        %>
            <p>Aucune classe disponible pour ce vol.</p>
        <% } else { %>
            <form action="reserver" method="post">
                <input type="hidden" name="idVol" value="<%= ((Vol)request.getAttribute("vol")).getIdVol() %>">
                
                <h2>Classes disponibles</h2>
                <table>
                    <tr>
                        <th>Classe</th>
                        <th>Prix Adulte</th>
                        <th>Prix Enfant</th>
                        <th>Places disponibles</th>
                        <th>Adultes</th>
                        <th>Enfants</th>
                    </tr>
                    
                    <% for (ClassVol classe : classesVol) { %>
                        <tr>
                            <td><%= classe.getNomClasse() %></td>  
                            <td><%= String.format("%.2f", classe.getPrixAdulte()) %> €</td>
                            <td><%= String.format("%.2f", classe.getPrixEnfant()) %> €</td>
                            <td><%= classe.getPlacesDisponibles() %></td>
                            <td>
                                <input type="number" name="adultes_<%= classe.getIdClasse() %>" 
                                       min="0" max="<%= classe.getPlacesDisponibles() %>" value="0">
                            </td>
                            <td>
                                <input type="number" name="enfants_<%= classe.getIdClasse() %>" 
                                       min="0" max="<%= classe.getPlacesDisponibles() %>" value="0">
                            </td>
                        </tr>
                    <% } %>
                </table>
                
                <button type="submit">Réserver</button>
            </form>
        <% } %>
    </div>
</body>
</html>