package ua.nure.grankina.periodicals.web.filters;

import org.junit.Before;
import org.junit.Test;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.entity.Role;
import ua.nure.grankina.periodicals.model.db.entity.User;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by USER-PC on 18.01.2017.
 */
public class FilterAdminTest extends FilterTest{
    private FilterAdmin filterAdmin = new FilterAdmin();
    private User admin = new User();

    @Before
    public void setUp(){
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        admin.setRole(Role.ADMIN);
    }

    @Test
    public void unauthorizedIsRedirectedToLoginPage() throws Exception {
        when(request.getPathInfo()).thenReturn(Path.ADMIN_LIST_PERIODICALS);
        filterAdmin.doFilter(request, response, chain);
        verify(response).sendRedirect(Path.redirectTo(Path.ADMIN_LOGIN));
    }

    @Test
    public void adminPassesFilter() throws Exception{
        when(request.getPathInfo()).thenReturn(Path.ADMIN_LIST_PERIODICALS);
        when(request.getSession().getAttribute("admin")).thenReturn(admin);
        filterAdmin.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    public void anyoneHasAccessToAdminLoginPage() throws Exception{
        when(request.getPathInfo()).thenReturn(Path.ADMIN_LOGIN);
        filterAdmin.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    public void stubsTest() throws ServletException{
        filterAdmin.init(initParam);
        filterAdmin.destroy();
    }

}