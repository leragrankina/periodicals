package ua.nure.grankina.periodicals.web.listeners;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Sets default locale on session creation
 *
 * Created by Valeriia on 08.01.2017.
 */

@WebListener()
public class ListenerSession implements HttpSessionListener{

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        String locale = httpSessionEvent.getSession().getServletContext().getInitParameter("javax.servlet.jsp.jstl.fmt.locale");
        httpSessionEvent.getSession().setAttribute("currentLocale", locale);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
