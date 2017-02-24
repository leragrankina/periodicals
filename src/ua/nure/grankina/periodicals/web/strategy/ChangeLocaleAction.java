package ua.nure.grankina.periodicals.web.strategy;

import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.db.entity.User;
import ua.nure.grankina.periodicals.web.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;

/**
 * Action for changing user's locale
 *
 * Created by Valeriia on 09.01.2017.
 */
public class ChangeLocaleAction implements Action{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        changeUserLocale(request, response);
        return request.getParameter("next");
    }

    public static void changeUserLocale(HttpServletRequest request, HttpServletResponse response){
        String currentLocale = (String) request.getSession().getAttribute(Attributes.CURRENT_LOCALE);
        String localeToBe = toggleLocale(currentLocale);
        changeJSTLLocale(request, localeToBe);
        if (Authentication.isUserAuthenticated(request, response)){
            setUserLocale(request, localeToBe);
        }
    }

    public static void setUserLocale(HttpServletRequest request, String locale){
        User currentUser = (User) request.getSession().getAttribute("user");
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        manager.setUserLocale(currentUser, locale);
    }

    private static String toggleLocale(String currentLocale){
        return currentLocale.equals("en")?"ru":"en";
    }

    public static void changeJSTLLocale(HttpServletRequest request, String localeToBe){
        Config.set(request.getSession(), Config.FMT_LOCALE, localeToBe);
        request.getSession().setAttribute(Attributes.CURRENT_LOCALE, localeToBe);
    }
}
