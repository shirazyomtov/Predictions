package serverlet;

import DTO.DTOActions.DTOActionInfo;
import DTO.DTOActions.DTOActionSerialize;
import DTO.DTOAllWorldsInfo;import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engineManager.EngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Details", urlPatterns = "/worldsName")
public class ShowDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        EngineManager engineManager = (EngineManager) getServletContext().getAttribute("manager");
        Gson gson = new GsonBuilder().registerTypeAdapter(DTOActionInfo.class, new DTOActionSerialize()).create();
        try {
            DTOAllWorldsInfo dtoWorldsName = engineManager.getDTOAllWorlds();
            String jsonResponse = gson.toJson(dtoWorldsName);
            try (PrintWriter out = resp.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }

        } catch (Exception e) {

        }

    }
}