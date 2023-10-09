package serverlet.executionPageServlet;

import DTO.DTOAllRequests;
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

@WebServlet(name = "Get current amount of entity", urlPatterns = "/getCurrentAmountOfEntity")
@MultipartConfig
public class GetCurrentAmountOfEntityServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineManager engineManager = (EngineManager) getServletContext().getAttribute("manager");
        Gson gson = new Gson();
        try {
            String worldName = req.getParameter("worldName");
            String entityName = req.getParameter("entityName");
            String userName = req.getParameter("userName");
            String executeID = req.getParameter("executeID");
            Integer amount =  engineManager.getCurrentAmountOfEntity(worldName, entityName, userName, Integer.parseInt(executeID));
            String jsonResponse = gson.toJson(amount);
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
