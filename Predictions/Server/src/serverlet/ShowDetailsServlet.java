package serverlet;

import DTO.DTOEntityInfo;
import utils.ServerUtils;
import com.google.gson.Gson;
import engineManager.EngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Details", urlPatterns = "/showDetails")
public class ShowDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        EngineManager engineManager = (EngineManager)getServletContext().getAttribute("manager");
        Gson gson = new Gson();
        try {
            DTOEntityInfo entityDetails = engineManager.getEntitiesDetails();
            String jsonResponse = gson.toJson(entityDetails);
           // System.out.println(jsonResponse);
            try (PrintWriter out = resp.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }

        } catch (Exception e) {

        }

    }
}