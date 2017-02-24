package ua.nure.grankina.periodicals.web.strategy;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.db.entity.Periodical;
import ua.nure.grankina.periodicals.model.db.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Action for subscribing user to a periodical
 *
 * Created by Valeriia on 03.01.2017.
 */
public class SubscribeAction implements Action{
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.strategy.SubscribeAction.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = (User) request.getSession().getAttribute("user");
        String periodicalId = request.getParameter("periodicalId");
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        Periodical periodical = manager.findPeriodical(Long.valueOf(periodicalId));
        try {
            log.debug("Subscribing to periodical of price --> " + periodical.getPrice());
            manager.subscribeUser(currentUser, periodical);
            request.getSession().setAttribute("successMessage", "subscribe_success");
            request.getSession().setAttribute("periodical_title", periodical.getTitle());
        }
        catch (IllegalArgumentException e){
            request.getSession().setAttribute("errorMessage", "subscribe_failure");
        }
        return "/pages" + Path.LIST_PERIODICALS ;
    }
}
