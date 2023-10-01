package serverlet;

import DTO.DTOWorldDefinitionInfo;
import com.google.gson.Gson;
import engineManager.EngineManager;
import worldManager.WorldManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Details", urlPatterns = "/showDetails")
public class ShowDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//        EngineManager engineManager = (EngineManager) getServletContext().getAttribute("manager");
//        //WorldManager worldManager = engineManager.getWorldManager();
//        Gson gson = new Gson();
//        try {
//          //  DTOWorldDefinitionInfo worldDefinitionInfo = worldManager.getWorldDefinition();
//         //   String jsonResponse = gson.toJson(worldDefinitionInfo);
//           // System.out.println(jsonResponse);
//            try (PrintWriter out = resp.getWriter()) {
//          //      out.print(jsonResponse);
//                out.flush();
//            }
//
//        } catch (Exception e) {
//
//        }

    }
}