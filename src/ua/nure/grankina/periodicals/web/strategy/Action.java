package ua.nure.grankina.periodicals.web.strategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Strategy in a Strategy pattern
 *
 * Created by Valeriia on 02.01.2017.
 */
public interface Action {
    String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
