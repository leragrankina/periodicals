package ua.nure.grankina.periodicals.web.strategy;

import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.db.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Action for confirming user registration
 *
 * Created by Valeriia on 15.01.2017.
 */
public class FinishRegistrationAction implements Action{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int expirationDays = getExpirationDays(request);
        String token = request.getParameter("token");
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        User user = manager.findUserByConfirmationToken(token);
        if (user != null) {
            if (linkHasExpired(expirationDays, user)) {
                return Path.redirectTo(Path.EXPIRED);
            } else {
                manager.unblockUser(user);
                manager.updateTimestamp(user);
                return Path.redirectTo(Path.SUCCESS);
            }
        } else{
            return Path.redirectTo(Path.EXPIRED);
        }
    }

    private boolean linkHasExpired(int expirationDays, User user) {
        return daysPassedSince(user.getTimestamp()) > expirationDays;
    }

    private int getExpirationDays(HttpServletRequest request) {
        return Integer.valueOf(request.getServletContext().getInitParameter("expirationDays"));
    }

    private int daysPassedSince(long timestamp){
        final int MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
        return (int) (System.currentTimeMillis() - timestamp) / MILLIS_IN_A_DAY;
    }
}
