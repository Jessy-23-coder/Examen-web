import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UIFormServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        String param = req.getParameter("plat");
        Cookie c = new Cookie("param", param);
        res.addCookie(c);
       out.println(param);
    }
}