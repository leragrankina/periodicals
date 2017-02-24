package ua.nure.grankina.periodicals.web.filters;


import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Encodes URL in UTF-8
 *
 * Created by Valeriia on 09.01.2017.
 */
@WebFilter(filterName = "FilterEncodeRequest")
public class FilterEncodeRequest implements Filter {
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.filters.FilterEncodeRequest.class);
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        log.debug("Encoding request body");
        request.setCharacterEncoding("UTF-8");
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
