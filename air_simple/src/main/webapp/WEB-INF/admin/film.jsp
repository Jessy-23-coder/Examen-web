<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="connexion.ConnectionDB" %>
<%@ page import="film.Film" %>

<!DOCTYPE html>
<html>
<head>
    <title>Liste des films</title>
</head>
    <body>
        <h1>Liste des films</h1>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <p style="color:red;"><%= error %></p>
        <%
            }
        %>

        <%
            List<Film> films = (List<Film>) request.getAttribute("films");
            if (films != null && !films.isEmpty()) {
        %>
            <table border="1">
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>categorie</th>
                    <th>date de sortie</th>
                    <th>Action</th></tr>
                <%
                    for (Film catRow : films) {
                %>
                    <tr>
                        <td><%= catRow.getId() %></td>
                        <td><%= catRow.getNom() %></td>
                        <td><%= catRow.getCategorie_id() %></td>
                        <td><%= catRow.getDate_sortie() %></td>
                        <td>
                            <a href="film?id=<%= catRow.getId() %>">Modifier</a>
                        </td>
                        <td>
                            <a href="film?id=<%= catRow.getId() %>&action=delete"
                            onclick="return confirm('Voulez-vous vraiment supprimer cette film ?');">
                            Supprimer
                            </a>
                        </td>

                    </tr>
                <%
                    }
                %>
            </table>
        <%
            } else {
        %>
            <p>Aucune film trouvée.</p>
        <%
            }
        %>

        <%
            String idStr = request.getParameter("id");
            Film catToEdit = null;
            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    catToEdit = Film.findById(id);
                } catch (Exception e) {
                    out.println("Erreur : " + e.getMessage());
                }
            }
        %>

        <h2><%= (catToEdit != null) ? "Modifier une film" : "Ajouter une film" %></h2>

        <form method="post" action="film">
            <%-- Si on modifie, on envoie l'id sinon champ caché vide --%>
            <input type="hidden" name="id" value="<%= (catToEdit != null) ? catToEdit.getId() : "" %>" />

            Nom : <input type="text" name="nom" value="<%= (catToEdit != null) ? catToEdit.getNom() : "" %>" required />
            categorie : <input type="text" name="categ_id" value="<%= (catToEdit != null) ? catToEdit.getCategorie_id() : "" %>" required />
            Date de sortie : <input type="text" name="date" value="<%= (catToEdit != null) ? catToEdit.getDate_sortie() : "" %>" required />
            <button type="submit"><%= (catToEdit != null) ? "Modifier" : "Ajouter" %></button>
        </form>

    </body>
</html>