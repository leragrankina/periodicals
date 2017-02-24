package ua.nure.grankina.periodicals.web.filters;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.SessionAttrsFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Clears session attributes after refresh
 *
 * Created by Valeriia on 06.01.2017.
 */
@WebFilter(filterName = "FilterClearSession")
public class FilterClearSession implements Filter {
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.filters.FilterClearSession.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        log.info(String.format("Intercepting '%s' request", request.getRequestURI()));
        log.info("Passing to chain...");
        if (request.getMethod().equals("GET")) {
            chain.doFilter(req, resp);
            SessionAttrsFactory.clear(request);
        } else{
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
