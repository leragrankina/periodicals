package ua.nure.grankina.periodicals.web.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.model.db.DBManager;

/**
 * Context listener
 *
 * Created by Valeriia
 */

public class ListenerContext implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(ListenerContext.class);

    public void contextDestroyed(ServletContextEvent event) {
        log("Servlet context destruction starts");
        log("Servlet context destruction finished");
    }

    public void contextInitialized(ServletContextEvent event) {
        log("Servlet context initialization starts");

        ServletContext servletContext = event.getServletContext();
        initLog4J(servletContext);
        servletContext.setAttribute(Attributes.DB_MANAGER, DBManager.getInstance());
        log("Servlet context initialization finished");
    }

    /**
     * Initializes log4j framework.
     *
     * @param servletContext
     */
    private void initLog4J(ServletContext servletContext) {
        log("Log4J initialization started");
        try {
            PropertyConfigurator.configure(
                    servletContext.getRealPath("WEB-INF/log4j.properties"));
            LOG.debug("Log4j has been initialized");
        } catch (Exception ex) {
            log("Cannot configure Log4j");
            ex.printStackTrace();
        }
        log("Log4J initialization finished");
    }

    private void log(String msg) {
        System.out.println("[ContextListener] " + msg);
    }
}
