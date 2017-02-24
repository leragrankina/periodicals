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
 * Filters protected pages for unauthorized access
 *
 * Created by Valeriia on 07.01.2017.
 */
@WebFilter(filterName = "FilterProtected")
public class FilterProtected implements Filter {
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.filters.FilterProtected.class);
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        User user = (User) request.getSession().getAttribute("user");
        log.debug(String.format("User '%s' tries to access '%s' page", user, request.getRequestURI()));
        boolean userValid = Authentication.isUserAuthenticated(request, response);
        if (userValid || request.getPathInfo().equals(Path.LOGIN)){
            if (userValid)
                log.info("Access allowed. Redirecting to --> " + request.getRequestURI());
            chain.doFilter(req, resp);
            return;
        }
        log.info("Access denied. Redirecting to /login");
        response.sendRedirect(Path.redirectTo(Path.LOGIN));
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
