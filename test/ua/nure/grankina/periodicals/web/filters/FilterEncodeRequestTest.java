package ua.nure.grankina.periodicals.web.filters;

import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by USER-PC on 18.01.2017.
 */
public class FilterEncodeRequestTest extends FilterTest{
    private FilterEncodeRequest filter = new FilterEncodeRequest();

    @Test
    public void requestEncodingIsSet() throws ServletException, IOException {
        filter.doFilter(request, response, chain);
        verify(request).setCharacterEncoding("UTF-8");
    }

    @Test
    public void chainIsCalled() throws ServletException, IOException {
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    public void stubsTest() throws ServletException {
        filter.init(initParam);
        filter.destroy();
    }

}