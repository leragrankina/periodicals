package ua.nure.grankina.periodicals;

import org.junit.Test;
import static org.junit.Assert.*;

import ua.nure.grankina.periodicals.web.strategy.Action;
import ua.nure.grankina.periodicals.web.strategy.NoAction;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Created by USER-PC on 18.01.2017.
 */
public class NoActionTest extends ActionTest{
    @Test
    public void executeTest() throws ServletException, IOException{
        Action noAction = new NoAction();
        String path = "pathInfo";
        when(request.getPathInfo()).thenReturn(path);
        assertEquals(path, noAction.execute(request, response));
    }
}
