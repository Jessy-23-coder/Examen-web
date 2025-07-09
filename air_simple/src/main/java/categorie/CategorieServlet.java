package categorie;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CategorieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String idStr = request.getParameter("id");

        try {
            if ("delete".equals(action) && idStr != null) {
                int id = Integer.parseInt(idStr);
                Categorie.deleteCategorie(id);
                response.sendRedirect(request.getContextPath() + "/categorie");
                return; 
            }

            List<Categorie> categories = Categorie.findAll(); 
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("categorie.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("categorie.jsp").forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nomCategorie = request.getParameter("nom");
        String idCategorieStr = request.getParameter("id");  

        try {
            if (idCategorieStr != null && !idCategorieStr.trim().isEmpty()) {
                int idCategorie = Integer.parseInt(idCategorieStr.trim());
                if (nomCategorie != null && !nomCategorie.trim().isEmpty()) {
                    Categorie.updateCategorie(idCategorie, nomCategorie.trim());
                }
            } else {
                if (nomCategorie != null && !nomCategorie.trim().isEmpty()) {
                    Categorie.insertCategorie(nomCategorie.trim());
                }
            }

            response.sendRedirect(request.getContextPath() + "/categorie");

        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors de l'ajout ou de la modification : " + e.getMessage());
            doGet(request, response);  
        }
    }


}
