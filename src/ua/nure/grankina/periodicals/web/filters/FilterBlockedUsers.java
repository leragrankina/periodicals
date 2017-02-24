package ua.nure.grankina.periodicals.web.filters;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filters all pages for blocked users
 *
 * Created by Valeriia on 13.01.2017.
 */
@WebFilter(filterName = "FilterBlockedUsers")
public class FilterBlockedUsers implements Filter {
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.filters.FilterBlockedUsers.class);
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        User currentUser = (User) request.getSession().getAttribute("user");
        boolean accessAllowed = currentUser == null || currentUser.isBlocked() == false;
        if (accessAllowed) {
            if (currentUser != null)
                log.debug(String.format("Access allowed to page %s for user %s who is %s", request.getPathInfo(), currentUser, currentUser.isBlocked()));
            chain.doFilter(req, resp);
        }
        else {
            response.sendRedirect(Path.redirectTo(Path.BLOCKED));
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
