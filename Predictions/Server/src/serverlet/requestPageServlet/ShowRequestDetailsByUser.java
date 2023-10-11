package serverlet.requestPageServlet;

import DTO.DTOAllRequests;
import DTO.DTOAllRequestsByUser;
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


@WebServlet(name = "Request details by user servlet", urlPatterns = "/requestDetailsByUser")
@MultipartConfig
public class ShowRequestDetailsByUser extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineManager engineManager = (EngineManager) getServletContext().getAttribute("manager");
        Gson gson = new Gson();
        try {
            String userName = req.getParameter("username");
            DTOAllRequestsByUser allRequestsByUser = engineManager.getAllRequestByUser(userName);
            String jsonResponse = gson.toJson(allRequestsByUser);
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
