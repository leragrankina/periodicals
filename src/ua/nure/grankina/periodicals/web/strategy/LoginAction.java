package ua.nure.grankina.periodicals.web.strategy;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.SessionAttrsFactory;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.db.entity.Role;
import ua.nure.grankina.periodicals.model.db.entity.User;
import ua.nure.grankina.periodicals.model.security.Security;
import ua.nure.grankina.periodicals.web.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Action for logging a user in
 *
 * Created by Valeriia on 03.01.2017.
 */
public class LoginAction implements Action{
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.strategy.LoginAction.class);
    List<String> nonFieldErrors = new ArrayList<>();

    protected String userAuthorized(HttpServletRequest request, HttpServletResponse response, User user){
        if (!user.isBlocked()) {
            if (isClientOrHasLoginAndPassword(user)) {
                log.debug("checkbox value --> " + request.getParameter("remember_me"));
                if (isRememberMeSet(request)) {
                    Authentication.rememberUser(request, response, user);
                }
                Authentication.authenticateUser(request, response, user);
                SessionAttrsFactory.clear(request);
                return Path.redirectTo(Path.LIST_PERIODICALS);
            } else{
                nonFieldErrors.add("no_use_error");
                return Path.redirectTo(request.getPathInfo());
            }
        }
        else{
            nonFieldErrors.add("blocked_account_error");
            return Path.redirectTo(request.getPathInfo());
        }
    }

    private boolean isClientOrHasLoginAndPassword(User user) {
        return user.getRole() == Role.CLIENT || !user.hasOnlyFBAccount();
    }

    private boolean isRememberMeSet(HttpServletRequest request) {
        return request.getParameter("remember_me") != null;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        nonFieldErrors = new ArrayList<>();
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String next = request.getParameter("next");
        debug(request, login, password, next);
        String forward = Path.redirectTo(request.getPathInfo());
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        User user = manager.findUser(login);
        log.debug("found user --> " + user);
        if (areUserCredsValid(password, user)) {
            forward = userAuthorized(request, response, user);
        } else {
            nonFieldErrors.add("no_use_error");
        }
        setReturnParameters(request);
        return forward;
    }

    private void setReturnParameters(HttpServletRequest request) {
        request.getSession().setAttribute("nonFieldErrors", nonFieldErrors);
    }

    private boolean areUserCredsValid(String password, User user) {
        String passwordHash = Security.hexHash(password);
        return user != null && user.getPassword().equals(passwordHash);
    }

    private void debug(HttpServletRequest request, String login, String password, String next) {
        log.debug("login --> " + login);
        log.debug("password --> " + password);
        log.debug("next --> " + next);
        log.debug("request page --> " + request.getPathInfo());
    }
}
