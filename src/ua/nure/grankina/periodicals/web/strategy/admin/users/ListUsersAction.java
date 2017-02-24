package ua.nure.grankina.periodicals.web.strategy.admin.users;

import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.web.strategy.Action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.nure.grankina.periodicals.Path.*;

/**
 * Lists all users who are not admins
 *
 * Created by Valeriia on 07.01.2017.
 */
public class ListUsersAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        request.setAttribute("users", manager.findClients());
        return forwardTo(ADMIN_LIST_USERS);
    }
}
