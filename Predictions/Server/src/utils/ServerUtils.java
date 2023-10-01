package utils;

import engineManager.EngineManager;
import worldManager.WorldManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ServerUtils implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        EngineManager logicManager = new EngineManager();
        sce.getServletContext().setAttribute("manager", logicManager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}