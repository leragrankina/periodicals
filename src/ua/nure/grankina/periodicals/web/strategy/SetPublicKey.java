package ua.nure.grankina.periodicals.web.strategy;

import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.web.captcha.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by USER-PC on 16.01.2017.
 */
public class SetPublicKey implements Action{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(Attributes.CAPTCHA_PUBLIC_KEY, Constants.PUBLIC_KEY);
        return Path.forwardTo(Path.REGISTER);
    }
}
