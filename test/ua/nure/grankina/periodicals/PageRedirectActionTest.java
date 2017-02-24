package ua.nure.grankina.periodicals;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import ua.nure.grankina.periodicals.web.strategy.Action;
import ua.nure.grankina.periodicals.web.strategy.PageRedirectAction;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by USER-PC on 18.01.2017.
 */
public class PageRedirectActionTest extends ActionTest{
    @Test
    public void execute() throws ServletException, IOException{
        String redirectTo = "redirect";
        String originalPath = "original";
        Action pageRedirect = new PageRedirectAction(redirectTo);
        when(request.getPathInfo()).thenReturn(originalPath);
        assertEquals(redirectTo, pageRedirect.execute(request, response));
    }
}
