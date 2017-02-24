package ua.nure.grankina.periodicals.web.filters;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.SessionAttrsFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Basic validating filter for null or empty input
 *
 * Created by Valeriia on 06.01.2017.
 */
@WebFilter(filterName = "FilterValidate")
public class FilterValidate implements Filter {
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.filters.FilterValidate.class);

    public void destroy() {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        List<String> parameters = SessionAttrsFactory.getParams(request);
        String forward = Path.getFullURI(request);
        log.info(String.format("Intercepting '%s%s' request", request.getMethod(), forward));
        if (request.getMethod().equals("POST")) {
            boolean hasErrors = validate(request, parameters);
            if (hasErrors) {
                log.debug(String.format("Input has errors. Redirecting to '%s'", forward));
                response.sendRedirect(forward);
            }
            else{
                chain.doFilter(req, resp);
            }
        } else {
            log.debug("Passing request to chain...");
            chain.doFilter(req, resp);
        }
    }

    private boolean validate(HttpServletRequest request, List<String> parameters) {
        boolean hasErrors = false;
        for (String param : parameters) {
            String value = request.getParameter(param);
            log.debug(String.format("Parameter '%s' is '%s'", param, value));
            if (value == null || value.trim().isEmpty()) {
                log.debug(String.format("Setting %sError attribute", param));
                request.getSession().setAttribute(param + "Error", "empty_field_error");
                request.getSession().setAttribute(param, value);
                hasErrors = true;
            } else {
                log.debug(String.format("Setting %s attribute", param));
                request.getSession().setAttribute(param, value);
            }
        }
        return hasErrors;
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
