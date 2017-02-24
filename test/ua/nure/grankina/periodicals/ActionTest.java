package ua.nure.grankina.periodicals;

import ua.nure.grankina.periodicals.model.db.DB;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;

/**
 * Created by USER-PC on 18.01.2017.
 */
public class ActionTest {
    protected HttpServletRequest request = mock(HttpServletRequest.class);
    protected HttpServletResponse response = mock(HttpServletResponse.class);
    protected HttpSession session = mock(HttpSession.class);
    protected ServletContext servletContext = mock(ServletContext.class);
    protected DB db = mock(DB.class);
}
