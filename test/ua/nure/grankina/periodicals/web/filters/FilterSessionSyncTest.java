package ua.nure.grankina.periodicals.web.filters;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.db.entity.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by USER-PC on 19.01.2017.
 */
public class FilterSessionSyncTest extends FilterTest{
    private FilterSessionSync filter = new FilterSessionSync();
    private DB db = mock(DB.class);
    private ServletContext con = mock(ServletContext.class);
    private User oldUser = mock(User.class);

    @Before
    public void setUp(){
        when(request.getServletContext()).thenReturn(con);
        when(con.getAttribute(Attributes.DB_MANAGER)).thenReturn(db);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void onDBChangeSessionChanges() throws ServletException, IOException{
        when(session.getAttribute("user")).thenReturn(oldUser);
        filter.doFilter(request, response, chain);
        verify(db).updateUser(oldUser);
    }

    @Test
    public void anonymousUserNoDBHit() throws ServletException, IOException{
        filter.doFilter(request, response, chain);
        verify(db, never()).updateUser(oldUser);
    }

    @Test
    public void stubsTest() throws ServletException{
        filter.init(initParam);
        filter.destroy();
    }
}