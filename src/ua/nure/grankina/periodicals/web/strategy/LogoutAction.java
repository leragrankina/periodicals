package ua.nure.grankina.periodicals.web.strategy;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.web.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Action for logging a user out
 *
 * Created by Valeriia on 03.01.2017.
 */
public class LogoutAction implements Action{
    private Logger log= Logger.getLogger(ua.nure.grankina.periodicals.web.strategy.LogoutAction.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Authentication.logout(request, response);
        return Path.redirectTo(Path.LIST_PERIODICALS);
    }
}
