package ua.nure.grankina.periodicals.web;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.web.strategy.Action;
import ua.nure.grankina.periodicals.web.strategy.ActionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Front controller
 *
 * Created by Valeriia on 01.01.2017.
 */
public class ServletController extends HttpServlet {
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.ServletController.class);
    {log.setLevel(Level.DEBUG);}

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service(request, response);
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Action action = ActionFactory.getAction(request);
        setNoCacheHeaders(response);
        try {
            String forward = action.execute(request, response);
            log.debug("Path info --> " + request.getPathInfo());
            log.debug("Forward page --> " + forward);
            if (forward.equals(request.getPathInfo())) {
                log.info("Forwarding to --> " + "/WEB-INF/jsp" + forward + ".jsp");
                request.getRequestDispatcher("/WEB-INF/jsp" + forward + ".jsp").forward(request, response);
            } else if (forward.startsWith("/WEB-INF/")) {
                request.getRequestDispatcher(forward).forward(request, response);
            } else {
                log.info("Redirecting to --> " + forward);
                response.sendRedirect(forward);
            }
        } catch(Exception e){
            log.error(e.getMessage());
            response.sendRedirect(Path.redirectTo(Path.ERROR));
        }

    }

    private void setNoCacheHeaders(HttpServletResponse response) {
        log.debug("Setting no cache headers...");
        response.setHeader("Cache-Control","no-store, no-cache, must-revalidate");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma","no-cache");
    }
}
