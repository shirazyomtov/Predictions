package serverlet;

import com.google.gson.Gson;
import engineManager.EngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Set current value of environment", urlPatterns = "/setCurrentValueOfEnvironment")
@MultipartConfig
public class SetValueOfEnvironment extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineManager engineManager = (EngineManager) getServletContext().getAttribute("manager");
        Gson gson = new Gson();
        try {
            String worldName = req.getParameter("worldName");
            String environmentName = req.getParameter("environmentName");
            String value = req.getParameter("value");
            String userName = req.getParameter("userName");
            String executeID = req.getParameter("executeID");
            engineManager.setCurrentValueOfEnvironment(worldName, environmentName, value,  userName, Integer.parseInt(executeID));
            // System.out.println(jsonResponse);
        } catch (Exception e) {
            resp.sendError(400, "Error processing the request: " + e.getMessage());
        }
    }
}
