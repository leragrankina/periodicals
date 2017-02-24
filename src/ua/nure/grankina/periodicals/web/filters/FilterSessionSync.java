package ua.nure.grankina.periodicals.web.filters;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.web.Authentication;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Refreshes user session from DB
 *
 * Created by Valeriia on 14.01.2017.
 */
public class FilterSessionSync implements Filter {
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.filters.FilterSessionSync.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (Authentication.isUserAuthenticated(request, response)){
            log.debug("User is authenticated. Synchronizing...");
            Authentication.refreshUser(request, response);
        }
        log.debug("Passing to chain...");
        filterChain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }
}
