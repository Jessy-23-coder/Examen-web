<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.ClassVol" %>
<%@ page import="model.Classe" %>
<%@ page import="model.VueVol" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <title>Gestion des classes pour le vol</title>
    <link rel="stylesheet" href="css/ajout_vol.css">
</head>
<body>
<div class="content">
    <h2>Gestion des classes pour le vol</h2>
            
    <form action="UpdateClassesVol" method="post">
        <!-- Champ caché pour l'ID du vol -->
        <input type="hidden" name="idVol" value="<%= request.getParameter("idVol") %>">
        
        <%
            List<Classe> classes = (List<Classe>) request.getAttribute("allClasses");
            if (classes != null && !classes.isEmpty()) { 
        %>
            <table>
                <thead>
                    <tr>
                        <th>Classe</th>
                        <th>Prix Adulte</th>
                        <th>Prix Enfant</th>
                        <th>Places disponibles</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Classe classe : classes) { %>
                        <tr>
                           <td>
                                <input 
                                    type="checkbox" 
                                    name="classe" 
                                    value="<%= classe.getIdClasse() %>"  
                                    id="classe_<%= classe.getIdClasse() %>"
                                    onchange="toggleClassInputs(this, <%= classe.getIdClasse() %>)"
                                >
                                <label for="classe_<%= classe.getIdClasse() %>">
                                    <%= classe.getNom() %>
                                </label>
                            </td>
                                
                            <td>
                                <input type="number" 
                                       name="adulte_<%= classe.getIdClasse() %>" 
                                       id="adulte_<%= classe.getIdClasse() %>" 
                                       min="0" step="0.01" 
                                       disabled required>
                            </td>

                            <td>
                                <input type="number" 
                                       name="enfant_<%= classe.getIdClasse() %>" 
                                       id="enfant_<%= classe.getIdClasse() %>" 
                                       min="0" step="0.01" 
                                       disabled required>
                            </td>

                            <td>
                                <input type="number" 
                                       name="place_<%= classe.getIdClasse() %>" 
                                       id="place_<%= classe.getIdClasse() %>" 
                                       min="0" 
                                       disabled required>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% } else { %>
            <p style="text-align:center; margin:20px 0;">Aucune classe disponible</p>
        <% } %>

        <div class="text-right">
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-save"></i> Enregistrer
            </button>
        </div>
    </form>

</div>

<script>
    // Active/désactive les champs selon l'état de la checkbox
    function toggleClassInputs(checkbox, classId) {
        const adulteInput = document.getElementById('adulte_' + classId);
        const enfantInput = document.getElementById('enfant_' + classId);
        const placeInput = document.getElementById('place_' + classId);
        
        if (checkbox.checked) {
            adulteInput.disabled = false;
            enfantInput.disabled = false;
            placeInput.disabled = false;
        } else {
            adulteInput.disabled = true;
            enfantInput.disabled = true;
            placeInput.disabled = true;
            // Réinitialiser les valeurs si on décoche
            adulteInput.value = '';
            enfantInput.value = '';
            placeInput.value = '';
        }
    }
</script>

</body>
</html>