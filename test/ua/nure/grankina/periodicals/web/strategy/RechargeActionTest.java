package ua.nure.grankina.periodicals.web.strategy;

import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import ua.nure.grankina.periodicals.ActionTest;
import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.entity.User;

import javax.servlet.ServletException;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by USER-PC on 19.01.2017.
 */
public class RechargeActionTest extends ActionTest{
    private RechargeAction action = new RechargeAction();
    private User user = mock(User.class);

    @Before
    public void setUp(){
        when(request.getSession()).thenReturn(session);
        when(servletContext.getAttribute(Attributes.DB_MANAGER)).thenReturn(db);
        when(request.getServletContext()).thenReturn(servletContext);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getPathInfo()).thenReturn(Path.RECHARGE);
    }

    @Test
    public void actionGetsOnlyNumericAmount() throws ServletException, IOException{
        verifyErrorSet("fifty", Attributes.ERR_NUMBERS_ONLY);
    }

    @Test
    public void negativeAmountNotSet() throws ServletException, IOException{
        verifyErrorSet("-50", Attributes.ERR_NOT_IN_RANGE);
    }

    @Test
    public void tooBigAmountNotSet() throws ServletException, IOException{
        verifyErrorSet("50000000000000", Attributes.ERR_NOT_IN_RANGE);
    }

    @Test
    public void normalAmountIsSet() throws ServletException, IOException{
        String param = "200";
        when(request.getParameter("amount")).thenReturn(param);
        String forward = action.execute(request, response);
        verify(db).increaseBalance(user, 200);
        assertEquals(Path.redirectTo(Path.PROFILE), forward);
    }

    @Test
    public void errorMessagesClearedAfterSuccess() throws ServletException, IOException{
        when(request.getParameter("amount")).thenReturn("200");
        when(session.getAttribute("amount")).thenReturn("200");
        action.execute(request, response);
        verify(session).removeAttribute("amount");
    }

    private void verifyErrorSet(String param, String error) throws ServletException, IOException {
        when(request.getParameter("amount")).thenReturn(param);
        String forward = action.execute(request, response);
        verify(session).setAttribute("amountError", error);
        assertEquals(Path.redirectTo(Path.RECHARGE), forward);
    }

}