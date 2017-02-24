package ua.nure.grankina.periodicals.web.filters;


import org.junit.Before;
import org.junit.Test;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.entity.User;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by USER-PC on 19.01.2017.
 */
public class FilterProtectedTest extends FilterTest{
    private Filter filter = new FilterProtected();

    @Before
    public void setUp(){
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getPathInfo()).thenReturn(Path.PROFILE);
    }

    @Test
    public void unauthorizedCannotAccessProtectedPages() throws IOException, ServletException {
        filter.doFilter(request, response, chain);
        verify(response).sendRedirect(Path.redirectTo(Path.LOGIN));
    }

    @Test
    public void authenticatedCanAccessProtected() throws IOException, ServletException {
        User user = new User();
        when(request.getSession().getAttribute("user")).thenReturn(user);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    public void anyoneHasAccessToLogin() throws IOException, ServletException {
        when(request.getPathInfo()).thenReturn(Path.LOGIN);
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    public void stubsTest() throws ServletException {
        filter.init(initParam);
        filter.destroy();
    }

}