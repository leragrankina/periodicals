package ua.nure.grankina.periodicals.web.filters;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Created by USER-PC on 19.01.2017.
 */
public class FilterValidateTest extends FilterTest{
    private FilterValidate filter = new FilterValidate();
    private String uri = "/pages/login";
    private String path = "/login";

    @Before
    public void setUp(){
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getPathInfo()).thenReturn(path);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void getMethodIsPassedToChain() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("GET");
        filter.doFilter(request, response, chain);
        verify(request, never()).getParameter(anyString());
        verifyFilterPassed();
    }

    @Test
    public void postMethodIsValidated() throws ServletException, IOException {
        setPost();
        setLogin();
        setPassword();
        filter.doFilter(request, response, chain);
        verify(request).getParameter("login");
        verify(request).getParameter("password");
    }

    @Test
    public void postWithEmptyParameterReturns() throws ServletException, IOException {
        setPost();
        setLogin();
        filter.doFilter(request, response, chain);
        verifyReturnToPage();
    }

    @Test
    public void emptyParameterErrorIsSet() throws ServletException, IOException {
        setPost();
        setLogin();
        filter.doFilter(request, response, chain);
        verifyErrorSet("passwordError");
    }

    @Test
    public void postWithNonEmptyParametersArePassedToChain() throws ServletException, IOException{
        setPost();
        setLogin();
        setPassword();
        filter.doFilter(request, response, chain);
        verifyFilterPassed();
    }

    @Test
    public void allEmptyParametersErrorsAreSet() throws ServletException, IOException{
        setPost();
        filter.doFilter(request, response, chain);
        verifyErrorSet("loginError");
        verifyErrorSet("passwordError");
    }

    @Test
    public void nonEmptyParametersErrorNotSet() throws ServletException, IOException {
        setPost();
        setLogin();
        filter.doFilter(request, response, chain);
        verifyErrorNotSet("loginError");
    }

    @Test
    public void stubsTest() throws ServletException{
        filter.init(initParam);
        filter.destroy();
    }

    private void setPassword() {
        when(request.getParameter("password")).thenReturn("password");
    }

    private void setLogin() {
        when(request.getParameter("login")).thenReturn("login");
    }

    private void setPost() {
        when(request.getMethod()).thenReturn("POST");
    }

    private void verifyReturnToPage() throws IOException {
        verify(response).sendRedirect(uri);
    }

    private void verifyFilterPassed() throws IOException, ServletException {
        verify(chain).doFilter(request, response);
    }

    private void verifyErrorSet(String field) {
        verify(session).setAttribute(field, "empty_field_error");
    }

    private void verifyErrorNotSet(String field){
        verify(session, never()).setAttribute(field, "empty_field_error");
    }

}