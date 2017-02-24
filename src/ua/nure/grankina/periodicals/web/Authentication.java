package ua.nure.grankina.periodicals.web;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.Token;
import ua.nure.grankina.periodicals.model.db.entity.User;
import ua.nure.grankina.periodicals.model.security.Security;
import ua.nure.grankina.periodicals.web.strategy.ChangeLocaleAction;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utility for user authentication
 *
 * Created by Valeriia on 09.01.2017.
 */
public class Authentication {
    private static Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.filters.FilterPersistentSession.class);

    public static void authenticateUser(HttpServletRequest request, HttpServletResponse response, User user){
        request.getSession().setAttribute(Attributes.USER, user);
        ChangeLocaleAction.changeJSTLLocale(request, user.getLang());
    }

    public static boolean isUserAnonymous(HttpServletRequest request, HttpServletResponse response){
        return request.getSession().getAttribute(Attributes.USER) == null;
    }

    public static boolean isUserAuthenticated(HttpServletRequest request, HttpServletResponse response){
        return !isUserAnonymous(request, response);
    }

    public static void rememberUser(HttpServletRequest request, HttpServletResponse response, User u){
        setRememberCookie(request, response, u);
    }

    private static void setRememberCookie(HttpServletRequest request, HttpServletResponse response, User user) {
        int tokenLength = Integer.valueOf(request.getServletContext().getInitParameter("tokenLength"));
        String token = new Token(tokenLength).getValue();
        String tokenHash = Security.hash(token);
        log.debug("setting remember_user cookie to --> " + token);
        addRememberCookie(request, response, token);
        log.debug("setting user.tokenHash cookie to --> " + tokenHash);
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        manager.setUserTokenHash(user, tokenHash);
    }

    public static void refreshUser(HttpServletRequest request, HttpServletResponse response){
        User user = (User) request.getSession().getAttribute(Attributes.USER);
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        manager.updateUser(user);
        authenticateUser(request, response, user);
    }

    private static void addRememberCookie(HttpServletRequest request, HttpServletResponse response, String token) {
        Cookie remember_user = new Cookie(Attributes.REMEMBER_ME, token);
        int cookieTimeout = Integer.valueOf(request.getServletContext().getInitParameter("cookieTimeout"));
        remember_user.setMaxAge(cookieTimeout);
        response.addCookie(remember_user);
    }

    public static boolean isRememberCookieSet(HttpServletRequest request){
        return getUserToken(request) != null;
    }

    public static User getRememberedUser(HttpServletRequest request){
        String token = getUserToken(request);
        log.debug("cookie value is " + token);
        String tokenHash = Security.hash(token);
        log.debug("token hash is " + tokenHash);
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        return manager.findUserByCookieToken(tokenHash);
    }

    private static String getUserToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cook : request.getCookies()) {
                if (Attributes.REMEMBER_ME.equals(cook.getName())) {
                    return cook.getValue();
                }
            }
        }
        return null;
    }

    public static void logout(HttpServletRequest request, HttpServletResponse response){
        for (Cookie cook : request.getCookies()) {
            if (cook.getName().equals(Attributes.REMEMBER_ME)) {
                log.debug("setting cookie's 'remember_me' age to 0");
                cook.setMaxAge(0);
                cook.setValue(null);
                response.addCookie(cook);
            }
        }
        request.getSession().invalidate();
    }

}
