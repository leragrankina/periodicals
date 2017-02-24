package ua.nure.grankina.periodicals.web.strategy.admin.periodicals;

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
 * Renders a form attributes for editing a periodical
 *
 * Created by Valeriia on 05.01.2017.
 */
public class ReadPeriodicalAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String periodicalId = request.getParameter("id");
        Periodical p = new Periodical();
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        if (periodicalId != null) {
            try{
                long id = Long.parseLong(periodicalId);
                p = manager.findPeriodical(id);
            } catch (NumberFormatException e){
                request.setAttribute("error", "Periodical id in wrong format");
                return Path.redirectTo(Path.ADMIN_LIST_PERIODICALS);
            }
        }
        if (request.getSession().getAttribute("title") == null)
            request.getSession().setAttribute("title", p.getTitle());
        if (request.getSession().getAttribute("price") == null)
            request.getSession().setAttribute("price", p.getPrice());
        if (request.getSession().getAttribute("theme") == null)
            request.getSession().setAttribute("theme", p.getTheme());
        if (request.getSession().getAttribute("period") == null)
            request.getSession().setAttribute("period", p.getPeriod());
        request.setAttribute("themes", manager.findThemes());
        request.setAttribute("periods", manager.findPeriods());
        return "/WEB-INF/jsp/admin/periodical.jsp";
    }
}
