package ua.nure.grankina.periodicals.web.filters;

import org.junit.Before;
import org.junit.Test;
import ua.nure.grankina.periodicals.ActionTest;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by USER-PC on 18.01.2017.
 */
public class FilterTest extends ActionTest {
    protected FilterChain chain = mock(FilterChain.class);
    protected FilterConfig initParam = mock(FilterConfig.class);
}
