package ua.nure.grankina.periodicals.web.strategy;

import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Action for listing user's subscriptions
 *
 * Created by Valeriia on 04.01.2017.
 */
public class ProfileAction implements Action{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = (User) request.getSession().getAttribute("user");
        request.setAttribute("subscribtions", currentUser.getPeriodicals());
        return Path.forwardTo(Path.PROFILE);
    }
}
