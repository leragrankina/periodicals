package ua.nure.grankina.periodicals.web.strategy;

import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.SessionAttrsFactory;
import ua.nure.grankina.periodicals.model.db.entity.Role;
import ua.nure.grankina.periodicals.model.db.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action for logging an admin in
 *
 * Created by Valeriia on 05.01.2017.
 */
public class AdminLoginAction extends LoginAction{
    @Override
    protected String userAuthorized(HttpServletRequest request, HttpServletResponse response, User user){
        if (user.getRole() == Role.ADMIN) {
            request.getSession().setAttribute("admin", user);
            SessionAttrsFactory.clear(request);
            return "/pages" + Path.ADMIN_LIST_PERIODICALS;
        }
        else{
            nonFieldErrors.add("not_admin_error");
            return "/pages" + Path.ADMIN_LOGIN;
        }
    }
}
