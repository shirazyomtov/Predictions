package serverlet;

import DTO.DTOActions.DTOActionInfo;
import DTO.DTOActions.DTOActionSerialize;
import DTO.DTOWorldDefinitionInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engineManager.EngineManager;
import worldManager.WorldManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Load xml servlet", urlPatterns = "/loadXml")
@MultipartConfig
public class LoadFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        EngineManager engineManager = (EngineManager) getServletContext().getAttribute("manager");
        Part xmlPart = req.getPart("xmlFile");
        Gson gson = new GsonBuilder().registerTypeAdapter(DTOActionInfo.class, new DTOActionSerialize()).create();

        try {
            DTOWorldDefinitionInfo dtoWorldDefinitionInfo =  engineManager.loadXMLAAndCheckValidation(xmlPart.getInputStream());
            String jsonResponse = gson.toJson(dtoWorldDefinitionInfo);
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