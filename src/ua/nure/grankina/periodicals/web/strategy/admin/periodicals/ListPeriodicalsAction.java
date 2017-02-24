package ua.nure.grankina.periodicals.web.strategy.admin.periodicals;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.web.strategy.Action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Lists all periodicals in DB
 *
 * Created by Valeriia on 05.01.2017.
 */
public class ListPeriodicalsAction implements Action {
    private Logger log = Logger.getLogger(ListPeriodicalsAction.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        request.setAttribute("periodicals", manager.findPeriodicals());
        log.debug("setting attribute 'periodicals' to --> " + manager.findPeriodicals().get(0));
        return Path.ADMIN_LIST_PERIODICALS;
    }
}
