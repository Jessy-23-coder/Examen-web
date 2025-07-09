<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="connexion.ConnectionDB" %>
<%@ page import="categorie.Categorie" %>

<!DOCTYPE html>
<html>
<head>
    <title>Liste des Catégories</title>
</head>
    <body>
        <h1>Liste des Catégories</h1>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <p style="color:red;"><%= error %></p>
        <%
            }
        %>

        <%
            List<Categorie> categories = (List<Categorie>) request.getAttribute("categories");
            if (categories != null && !categories.isEmpty()) {
        %>
            <table border="1">
                <tr><th>ID</th><th>Nom</th><th>Action</th></tr>
                <%
                    for (Categorie catRow : categories) {
                %>
                    <tr>
                        <td><%= catRow.getID() %></td>
                        <td><%= catRow.getName() %></td>
                        <td>
                            <a href="categorie?id=<%= catRow.getID() %>">Modifier</a>
                        </td>
                        <td>
                            <a href="categorie?id=<%= catRow.getID() %>&action=delete"
                            onclick="return confirm('Voulez-vous vraiment supprimer cette catégorie ?');">
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
            <p>Aucune catégorie trouvée.</p>
        <%
            }
        %>

        <%
            String idStr = request.getParameter("id");
            Categorie catToEdit = null;
            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    catToEdit = Categorie.findById(id);
                } catch (Exception e) {
                    out.println("Erreur : " + e.getMessage());
                }
            }
        %>

        <h2><%= (catToEdit != null) ? "Modifier une catégorie" : "Ajouter une catégorie" %></h2>

        <form method="post" action="categorie">
            <%-- Si on modifie, on envoie l'id sinon champ caché vide --%>
            <input type="hidden" name="id" value="<%= (catToEdit != null) ? catToEdit.getID() : "" %>" />

            Nom : <input type="text" name="nom" value="<%= (catToEdit != null) ? catToEdit.getName() : "" %>" required />

            <button type="submit"><%= (catToEdit != null) ? "Modifier" : "Ajouter" %></button>
        </form>

    </body>
</html>