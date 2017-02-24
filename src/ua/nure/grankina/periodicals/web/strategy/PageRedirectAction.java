package ua.nure.grankina.periodicals.web.strategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Action for redirecting to another servlet
 *
 * Created by Valeriia on 05.01.2017.
 */
public class PageRedirectAction implements Action{
    private String redirect;

    public PageRedirectAction(String page){
        redirect = page;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return redirect;
    }
}
