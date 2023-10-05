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

@WebServlet(name = "Set thread count", urlPatterns = "/setThreadCount")
@MultipartConfig
public class SetThreadCountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineManager engineManager = (EngineManager) getServletContext().getAttribute("manager");
        try {
            String threadCount = req.getParameter("threadCount");
            engineManager.setThreadCount(Integer.parseInt(threadCount));
        } catch (Exception e) {
            resp.sendError(400, "Error processing the request: " + e.getMessage());
        }
    }
}
