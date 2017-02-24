package ua.nure.grankina.periodicals.web.strategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Default action
 *
 * Created by Valeriia on 02.01.2017.
 */
public class NoAction implements Action{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return request.getPathInfo();
    }
}
