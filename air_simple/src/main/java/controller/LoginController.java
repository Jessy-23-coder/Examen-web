package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Admin;
import model.Client;

public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getServletPath();
        
        try {
            switch(action) {
                case "/logout":
                    handleLogout(request, response);
                    break;
                default:
                    if (!response.isCommitted()) {
                        response.sendRedirect(request.getContextPath() + "/index.jsp");
                    }
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getServletPath();
        
        try {
            if ("/login".equals(action)) {
                handleLogin(request, response);
            } else if (!response.isCommitted()) {
                response.sendError(404);
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String mail = request.getParameter("mail");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");

        try {
            if ("admin".equals(userType)) {
                Admin admin = Admin.authenticate(mail, password);
                if (admin != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("admin", admin);
                    response.sendRedirect(request.getContextPath() + "/vols");
                    return;
                }
            } else if ("client".equals(userType)) {
                Client client = Client.authenticate(mail, password);
                if (client != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("client", client);
                    response.sendRedirect(request.getContextPath() + "/client");
                    return;
                }
            }

            // Si l'authentification échoue
            request.setAttribute("errorMessage", "Email ou mot de passe incorrect");
            request.getRequestDispatcher("/index.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage", "Erreur lors de l'authentification");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {
        e.printStackTrace();
        request.setAttribute("errorMessage", "Erreur: " + e.getMessage());
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}