package ua.nure.grankina.periodicals.web.filters;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.entity.User;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by USER-PC on 18.01.2017.
 */
public class FilterBlockedUsersTest extends FilterTest{
    private FilterBlockedUsers filterBlocked = new FilterBlockedUsers();
    private User blockedUser = new User();
    private User unblockedUser = new User();
    {
        blockedUser.setBlocked(true);
        unblockedUser.setBlocked(false);
    }

    @Before
    public void setUp(){
        when(request.getSession()).thenReturn(mock(HttpSession.class));
    }

    @Test
    public void anonymousUserCanAccessPages() throws ServletException, IOException {
       filterBlocked.doFilter(request, response, chain);
       verify(chain).doFilter(request, response);
    }

    @Test
    public void blockedUserUserCannotAccessAnyPage() throws IOException, ServletException {
        when(request.getSession().getAttribute("user")).thenReturn(blockedUser);
        filterBlocked.doFilter(request, response, chain);
        verify(response).sendRedirect(Path.redirectTo(Path.BLOCKED));
    }

    @Test
    public void unblockedUserUserCanAccessPages() throws IOException, ServletException {
        when(request.getSession().getAttribute("user")).thenReturn(unblockedUser);
        filterBlocked.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    public void stubsTest() throws ServletException{
        filterBlocked.init(initParam);
        filterBlocked.destroy();
    }

}