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
import java.io.PrintWriter;

@WebServlet(name = "Get current value of environment", urlPatterns = "/getCurrentValueOfEnvironment")
@MultipartConfig
public class GetCurrentValueOfEnvironment extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineManager engineManager = (EngineManager) getServletContext().getAttribute("manager");
        Gson gson = new Gson();
        try {
            String worldName = req.getParameter("worldName");
            String environmentName = req.getParameter("environmentName");
            String userName = req.getParameter("userName");
            String executeID = req.getParameter("executeID");
            Object value = engineManager.getCurrentValueOfEnvironment(worldName, environmentName, userName, Integer.parseInt(executeID));
            String jsonResponse = gson.toJson(value);
            // System.out.println(jsonResponse);
            try (PrintWriter out = resp.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }
        } catch (Exception e) {
            resp.sendError(400, "Error processing the request: " + e.getMessage());
        }
    }
}
