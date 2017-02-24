package ua.nure.grankina.periodicals;

import org.junit.Test;
import ua.nure.grankina.periodicals.web.captcha.Constants;
import ua.nure.grankina.periodicals.web.strategy.SetPublicKey;

import static org.mockito.Mockito.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by USER-PC on 18.01.2017.
 */
public class SetPublicKeyActionTest {
    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    public void executeTest() throws ServletException, IOException{
        new SetPublicKey().execute(request, response);
        verify(request).setAttribute(Attributes.CAPTCHA_PUBLIC_KEY, Constants.PUBLIC_KEY);
    }
}
