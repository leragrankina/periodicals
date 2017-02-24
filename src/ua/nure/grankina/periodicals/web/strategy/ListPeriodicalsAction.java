package ua.nure.grankina.periodicals.web.strategy;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.db.entity.Periodical;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Action for listing all periodicals in DB
 *
 * Created by Valeriia on 02.01.2017.
 */
public class ListPeriodicalsAction implements Action{
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.strategy.ListPeriodicalsAction.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        log.debug("ListPeriodicals executes " + request.getPathInfo());
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        List<Periodical> periodicals = manager.findPeriodicals();
        if (request.getParameterMap().containsKey("theme")){
            String themeValue = request.getParameter("theme");
            periodicals = manager.filterPeriodicalsBy("theme", themeValue);
        }

        if (request.getParameterMap().containsKey("title")){
            String titleValue = request.getParameter("title");
            periodicals = manager.filterPeriodicalsBy("title", titleValue);
        }

        if (request.getParameterMap().containsKey("sort")){
            String sortParam = request.getParameter("sort");
            String orderParam = request.getParameter("order");
            periodicals = manager.sortPeriodicalsBy(sortParam, orderParam);
        }
        log.debug("Setting request attr 'periodicals' to");
        request.setAttribute("periodicals", periodicals);
        return Path.forwardTo(request.getPathInfo());
    }
}
