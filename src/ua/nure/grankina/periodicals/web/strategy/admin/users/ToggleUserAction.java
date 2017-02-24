package ua.nure.grankina.periodicals.web.strategy.admin.users;

import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.db.entity.User;
import ua.nure.grankina.periodicals.web.strategy.Action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Action for blocking or unblocking a user
 *
 * Created by Valeriia on 07.01.2017.
 */
public class ToggleUserAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        long id = Long.valueOf(idParam);
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        User user = manager.findUser(id);
        if (user.isBlocked()) {
            manager.unblockUser(user);
            request.getSession().setAttribute("successMessage", "user_unblocked");
        }
        else{
            manager.blockUser(user);
            request.getSession().setAttribute("errorMessage", "user_blocked");
        }
        request.getSession().setAttribute("user_login", user.getLogin());
        return Path.redirectTo(Path.ADMIN_LIST_USERS);
    }
}
