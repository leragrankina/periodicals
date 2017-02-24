package ua.nure.grankina.periodicals.web.listeners;

import org.junit.Test;
import ua.nure.grankina.periodicals.ActionTest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by USER-PC on 19.01.2017.
 */
public class ListenerSessionTest extends ActionTest {
    private ListenerSession listener = new ListenerSession();

    @Test
    public void currentLocaleSetOnSessionCreated(){
        HttpSessionEvent event = mock(HttpSessionEvent.class);
        HttpSession session = mock(HttpSession.class);
        String initLang = "en";
        when(session.getServletContext()).thenReturn(mock(ServletContext.class));
        when(session.getServletContext().getInitParameter("javax.servlet.jsp.jstl.fmt.locale")).thenReturn(initLang);
        when(event.getSession()).thenReturn(session);
        listener.sessionCreated(event);
        verify(session).setAttribute("currentLocale", initLang);
    }
}