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
 * Filters admin pages for unauthorized access
 *
 * Created by Valeriia on 05.01.2017.
 */
@WebFilter(filterName = "FilterAdmin")
public class FilterAdmin implements Filter {
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.filters.FilterAdmin.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        User admin = (User) request.getSession().getAttribute("admin");
        log.debug(String.format("User %s tries to access %s", admin, request.getPathInfo()));
        if (admin != null) {
            chain.doFilter(req, resp);
        }
        else if(request.getPathInfo().equals(Path.ADMIN_LOGIN)){
            chain.doFilter(req, resp);
        }
        else{
            response.sendRedirect(Path.redirectTo(Path.ADMIN_LOGIN));
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
