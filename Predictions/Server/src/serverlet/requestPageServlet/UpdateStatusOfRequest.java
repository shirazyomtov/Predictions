package serverlet.requestPageServlet;

import engineManager.EngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "update request status", urlPatterns = "/updateStatus")
@MultipartConfig
public class UpdateStatusOfRequest extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineManager engineManager = (EngineManager) getServletContext().getAttribute("manager");
        String status = req.getParameter("status");
        String requestId = req.getParameter("requestId");
        engineManager.updateRequestStatus(requestId, status);
    }
}