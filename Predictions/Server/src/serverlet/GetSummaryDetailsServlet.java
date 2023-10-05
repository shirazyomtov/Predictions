package serverlet;

import DTO.DTOEntitiesAndEnvironmentInfo;
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

@WebServlet(name = "Show final details about entities and environments", urlPatterns = "/getSummaryDetails")
@MultipartConfig
public class GetSummaryDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineManager engineManager = (EngineManager) getServletContext().getAttribute("manager");
        Gson gson = new Gson();
        try {
            String worldName = req.getParameter("worldName");
            String userName = req.getParameter("userName");
            String executeID = req.getParameter("executeID");
            DTOEntitiesAndEnvironmentInfo dtoEntitiesAndEnvironmentInfo = engineManager.getFinalDetailsSummaryPage(worldName, userName, Integer.parseInt(executeID));
            String jsonResponse = gson.toJson(dtoEntitiesAndEnvironmentInfo);
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
