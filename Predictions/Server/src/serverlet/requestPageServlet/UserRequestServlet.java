package serverlet.requestPageServlet;

import engineManager.EngineManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "add request user", urlPatterns = "/addUserRequest")
@MultipartConfig
public class UserRequestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EngineManager engineManager = (EngineManager) getServletContext().getAttribute("manager");
        String name = req.getParameter("simulationName");
        String amountOfSimulation = req.getParameter("amountOfSimulation");
        String ticks = req.getParameter("ticks");
        String seconds = req.getParameter("seconds");
        String user = req.getParameter("user");
        String userName =  req.getParameter("username");
        engineManager.addAllocation(name, amountOfSimulation, ticks, seconds, user, userName);
    }
}
