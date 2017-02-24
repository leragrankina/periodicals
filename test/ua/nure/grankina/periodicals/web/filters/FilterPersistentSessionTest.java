package ua.nure.grankina.periodicals.web.filters;

import org.junit.Before;
import org.junit.Test;
import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.db.entity.User;
import ua.nure.grankina.periodicals.model.security.Security;

import static org.mockito.Mockito.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by USER-PC on 19.01.2017.
 */
public class FilterPersistentSessionTest extends FilterTest{
    private FilterPersistentSession filter = new FilterPersistentSession();
    private DB db = mock(DB.class);
    private String hash = "hash";
    private HttpSession session = mock(HttpSession.class);
    private User user = mock(User.class);
    private ServletContext con = mock(ServletContext.class);

    @Before
    public void setUp(){
        when(request.getPathInfo()).thenReturn(Path.LIST_PERIODICALS);
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(con);
        when(request.getServletContext().getAttribute(Attributes.DB_MANAGER)).thenReturn(db);
    }

    @Test
    public void ifRememberCookieSetThenLogin() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("GET");
        when(db.findUserByCookieToken(Security.hash(hash))).thenReturn(user);
        Cookie cook = new Cookie(Attributes.REMEMBER_ME, hash);
        when(request.getCookies()).thenReturn(new Cookie[]{cook});
        filter.doFilter(request, response, chain);
        verify(session).setAttribute("user", user);
    }

    @Test
    public void ifRememberCookieNotSetPassToChain() throws ServletException, IOException{
        when(request.getMethod()).thenReturn("GET");
        filter.doFilter(request, response, chain);
        verify(session, never()).setAttribute("user", user);
        verify(chain).doFilter(request, response);
    }

    @Test
    public void ifRemeberedCookieHashNotFoundLogout() throws ServletException, IOException{
        when(request.getMethod()).thenReturn("GET");
        Cookie cook = mock(Cookie.class);
        when(cook.getName()).thenReturn(Attributes.REMEMBER_ME);
        when(cook.getValue()).thenReturn("jfkdjf");
        when(request.getCookies()).thenReturn(new Cookie[]{cook});
        when(session.getAttribute("user")).thenReturn(user);
        filter.doFilter(request, response, chain);
        verify(session).invalidate();
        verify(cook).setMaxAge(0);
    }

    @Test
    public void postMethodIsPassedToChain() throws ServletException, IOException{
        when(request.getMethod()).thenReturn("POST");
        filter.doFilter(request, response, chain);
        verify(session, never()).setAttribute(anyString(), anyObject());
        verify(chain).doFilter(request, response);
    }

    @Test
    public void onLogoutSessionNotSet() throws ServletException, IOException{
        verifyUserNotSet(Path.LOGOUT);
    }

    @Test
    public void onLoginSessionNotSet() throws ServletException, IOException{
        verifyUserNotSet(Path.LOGIN);
    }

    @Test
    public void stubsTest() throws ServletException{
        filter.init(initParam);
        filter.destroy();
    }

    private void verifyUserNotSet(String path) throws ServletException, IOException {
        when(request.getMethod()).thenReturn("GET");
        when(request.getPathInfo()).thenReturn(path);
        filter.doFilter(request, response, chain);
        verify(session, never()).setAttribute(anyString(), anyObject());
        verify(chain).doFilter(request, response);
    }

}