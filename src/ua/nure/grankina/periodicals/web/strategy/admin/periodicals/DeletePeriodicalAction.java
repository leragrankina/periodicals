package ua.nure.grankina.periodicals.web.strategy.admin.periodicals;

import org.apache.log4j.*;
import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.db.entity.Periodical;
import ua.nure.grankina.periodicals.web.strategy.Action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Deletes a periodical
 *
 * Created by Valeriia on 05.01.2017.
 */
public class DeletePeriodicalAction implements Action {
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.strategy.admin.periodicals.DeletePeriodicalAction.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idValue = request.getParameter("id");
        log.debug("Trying to delete periodical of id --> " + idValue);
        try{
            long id = Long.valueOf(idValue);
            DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
            Periodical p = manager.findPeriodical(id);
            log.debug(String.format("Found periodical %s of title '%s'", p.toString(), p.getTitle()));
            request.getSession().setAttribute("successMessage", "periodical_deleted");
            request.getSession().setAttribute("periodical_title", p.getTitle());
            manager.deletePeriodical(id);
        } catch (NumberFormatException e){
            log.debug("Cannot parse id");
            request.getSession().setAttribute("errorMessage", "periodical_not_deleted");
        }
        return Path.redirectTo(Path.ADMIN_LIST_PERIODICALS);
    }
}
