package ua.nure.grankina.periodicals.web.filters;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.entity.User;
import ua.nure.grankina.periodicals.web.Authentication;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filters all pages for users with remember-me token set
 *
 * Created by Valeriia on 09.01.2017.
 */
@WebFilter(filterName = "FilterPersistentSession")
public class FilterPersistentSession implements Filter {
    private Logger log = Logger.getLogger(FilterPersistentSession.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        log.debug("user requested page --> " + request.getPathInfo());
        if (request.getPathInfo().equals(Path.LOGOUT) || request.getPathInfo().equals(Path.LOGIN)){
            log.debug("passing to chain");
        } else {
            if (request.getMethod().equals("GET")) {
                log.debug("user is not authenticated");
                if (Authentication.isRememberCookieSet(request)) {
                    log.debug("request has remember_me cookie");
                    User u = Authentication.getRememberedUser(request);
                    if (u != null) {
                        log.debug("user with such token found --> " + u.getTokenHash());
                        log.debug("authenticating user --> " + u.getLogin());
                        Authentication.authenticateUser(request, response, u);
                    } else{
                        Authentication.logout(request, response);
                        log.debug("user with such token not found");
                    }
                } else
                    log.debug("cookie remember_me is not set.");
            } else
                log.debug("user is --> " + request.getSession().getAttribute("user"));
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
