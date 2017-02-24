package ua.nure.grankina.periodicals.web.filters;

import org.junit.Before;
import org.junit.Test;
import ua.nure.grankina.periodicals.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by USER-PC on 19.01.2017.
 */
public class FilterClearSessionTest extends FilterTest{
    private FilterClearSession filter = new FilterClearSession();

    @Before
    public void setUp(){
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void afterServingPageSessionIsCleared() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("GET");
        when(request.getPathInfo()).thenReturn(Path.LOGIN);
        when(request.getSession().getAttribute("loginError")).thenReturn("empty_field_error");
        filter.doFilter(request, response, chain);
        verify(request.getSession()).removeAttribute("loginError");
    }

    @Test
    public void postRequestsAreNotCleared() throws ServletException, IOException{
        when(request.getMethod()).thenReturn("POST");
        when(request.getPathInfo()).thenReturn(Path.LOGIN);
        when(request.getSession().getAttribute("loginError")).thenReturn("empty_field_error");
        filter.doFilter(request, response, chain);
        verify(request.getSession(), never()).removeAttribute("loginError");
    }

    @Test
    public void stubsTest() throws ServletException{
        filter.init(initParam);
        filter.destroy();
    }
}