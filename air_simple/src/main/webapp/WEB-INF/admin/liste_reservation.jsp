<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="model.Reservation" %>
<%@ page isErrorPage="false" errorPage="/error.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des Réservations</title>
    <link rel="stylesheet" href="css/index.css">
</head>
<body>
<div class="container">
        <div class="content">
    <h1>Liste des Réservations</h1>
    
    <jsp:useBean id="allresa" type="java.util.List<model.Reservation>" scope="request"/>
    
    <%-- Debug --%>
    <div style="display:none;">
        <p>Nombre de réservations: ${allresa.size()}</p>
        <p>Session ID: <%= session.getId() %></p>
        <p>Request URI: <%= request.getRequestURI() %></p>
    </div>
    
    <% if (allresa == null || allresa.isEmpty()) { %>
        <p class="error">Aucune réservation à afficher</p>
    <% } else { %>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Vol</th>
                    <th>Date</th>
                    <th>Adultes</th>
                    <th>Enfants</th>
                    <th>Total</th>
                    <th>Statut</th>
                </tr>
            </thead>
            <tbody>
                <% for (Reservation res : allresa) { %>
                    <tr>
                        <td><%= res.getIdReservation() %></td>
                        <td><%= res.getIdVol() %></td>
                        <td><%= res.getDateReservation() != null ? res.getDateReservation().toString() : "N/A" %></td>
                        <td><%= res.getAdulte() %></td>
                        <td><%= res.getEnfant() %></td>
                        <td><%= res.getTouslespersonnes() %></td>
                        <td><%= res.getStatut() != null ? res.getStatut() : "N/A" %></td>
                        <td class="action-buttons">
                            <% if (Reservation.STATUT_NON_PAYE.equals(res.getStatut())) { %>
                                <button class="btn btn-pay" 
                                        onclick="if(confirm('Confirmer le paiement de cette réservation ?')) { 
                                            window.location.href='payerReservation?id=<%= res.getIdReservation() %>'
                                        }">
                                    Payer
                                </button>
                            <% } else if (Reservation.STATUT_PAYEE.equals(res.getStatut())) { %>
                                <span class="status-paid">Payée</span>
                            <% } %>
                        </td>

                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>
</div>
</div>
</body>
</html>