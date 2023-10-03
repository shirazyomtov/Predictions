package serverlet;

import engineManager.EngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import userManager.UserManager;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "Login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(req);
        UserManager userManager = (UserManager) getServletContext().getAttribute("userManager");
        if (usernameFromSession == null) {

            String usernameFromParameter = req.getParameter("username");
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                usernameFromParameter = usernameFromParameter.trim();

                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                       resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                       resp.getOutputStream().print(errorMessage);
                    }
                    else {
                        userManager.addUser(usernameFromParameter);
                        req.getSession(true).setAttribute("username", usernameFromParameter);

                        System.out.println("On login, request URI is: " + req.getRequestURI());
                        resp.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
