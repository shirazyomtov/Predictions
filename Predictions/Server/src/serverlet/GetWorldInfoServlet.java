package serverlet;

import DTO.DTOAllSimulations;
import DTO.DTOWorldInfo;
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

@WebServlet(name = "Get world info", urlPatterns = "/getWorldInfo")
@MultipartConfig
public class GetWorldInfoServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineManager engineManager = (EngineManager) getServletContext().getAttribute("manager");
        Gson gson = new Gson();
        try {
            String worldName = req.getParameter("worldName");
            String simulationId = req.getParameter("simulationId");
            DTOWorldInfo dtoWorldInfo =  engineManager.getDTOWorldInfo(worldName, Integer.parseInt(simulationId));
            String jsonResponse = gson.toJson(dtoWorldInfo);
            try (PrintWriter out = resp.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }
        } catch (Exception e) {
            resp.sendError(400, "Error processing the request: " + e.getMessage());
        }
    }
}
