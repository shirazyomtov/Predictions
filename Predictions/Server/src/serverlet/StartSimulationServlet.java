package serverlet;

import engineManager.EngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "Start Simulation", urlPatterns = "/startSimulation")
@MultipartConfig
public class StartSimulationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineManager engineManager = (EngineManager) getServletContext().getAttribute("manager");
        try {
            String worldName = req.getParameter("worldName");
            String userName = req.getParameter("userName");
            String requestId = req.getParameter("requestId");
            String executeID = req.getParameter("executeID");
            engineManager.defineSimulation(worldName, userName, Integer.parseInt(requestId), Integer.parseInt(executeID));
        }
        catch (Exception e) {
            resp.sendError(400, "Error processing the request: " + e.getMessage());
        }
    }
}
