package ua.nure.grankina.periodicals.web.strategy;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.SessionAttrsFactory;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.security.Security;
import ua.nure.grankina.periodicals.model.security.Validator;
import ua.nure.grankina.periodicals.model.mail.Email;
import ua.nure.grankina.periodicals.web.captcha.Constants;
import ua.nure.grankina.periodicals.web.json.JSONParser;
import ua.nure.grankina.periodicals.web.net.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Action for registering a user
 *
 * Created by Valeriia on 04.01.2017.
 */
public class RegisterAction implements Action{
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.strategy.RegisterAction.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        String email = request.getParameter("email");
        log.debug("Got email --> " + email);
        List<String> nonFieldErrors = new ArrayList<>();
        List<String> password2Error = new ArrayList<>();
        List<String> loginError = new ArrayList<>();
        List<String> emailError = new ArrayList<>();
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        boolean inputValid = validateInput(login, password1, password2, email, nonFieldErrors, password2Error, loginError, emailError, manager);
        boolean captchaSuccessful = verifyCaptcha(request, nonFieldErrors);
        if (inputValid && captchaSuccessful){
            registerUser(request, login, password2, email);
            clearReturnParameters(request);
            return Path.redirectTo(Path.ACTIVATE);
        } else {
            setReturnParameters(request, nonFieldErrors, password2Error, loginError, emailError);
            return Path.redirectTo(Path.REGISTER);
        }
    }

    private void registerUser(HttpServletRequest request, String login, String password2, String email) {
        int saltLength = Integer.valueOf(request.getServletContext().getInitParameter("saltLength")); //TODO as static fields
        String salt = Security.getRandomString(saltLength);
        long timestamp = System.currentTimeMillis();
        String confirmationToken = genConfirmationToken(email, salt, timestamp);
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        manager.insertUser(login, password2, email, timestamp, salt);
        Email.sendVerification(email, confirmationToken, (String) request.getSession().getAttribute(Attributes.CURRENT_LOCALE));
    }

    private String genConfirmationToken(String email, String salt, long timestamp) {
        String tokenRaw = email + timestamp + salt;
        return Security.hexHash(tokenRaw);
    }

    private void clearReturnParameters(HttpServletRequest request) {
        SessionAttrsFactory.clear(request);
    }

    private void setReturnParameters(HttpServletRequest request, List<String> nonFieldErrors, List<String> password2Error, List<String> loginError, List<String> emailError) {
        request.getSession().removeAttribute("password1");
        request.getSession().removeAttribute("password2");
        request.getSession().setAttribute("nonFieldErrors", nonFieldErrors);
        request.getSession().setAttribute("loginError", loginError);
        request.getSession().setAttribute("password2Error", password2Error);
        request.getSession().setAttribute("emailError", emailError);
    }

    private boolean validateInput(String login, String password1, String password2, String email, List<String> nonFieldErrors, List<String> password2Error, List<String> loginError, List<String> emailError, DB manager) {
        boolean loginValid = validateLogin(login, loginError, nonFieldErrors, manager);
        boolean passwordsValid = validatePasswords(password1, password2, nonFieldErrors, password2Error);
        boolean emailValid = validateEmail(email, emailError, manager);
        return loginValid && passwordsValid && emailValid;
    }

    private boolean isLoginUnique(String login, List<String> nonFieldErrors, DB manager) {
        if (manager.userExists(login)){
            nonFieldErrors.add("user_exists_error");
            return false;
        }
        return true;
    }

    private boolean validatePasswords(String password1, String password2, List<String> nonFieldErrors, List<String> password2Error) {
        boolean noErrors = true;
        if (Validator.doesNotMatch(password1, password2)){
            nonFieldErrors.add("no_match_error");
            noErrors = false;

        }
        if (Validator.isLessMinLength(password2, 6)){
            password2Error.add("password_too_short_error");
            noErrors = false;
        }

        if (Validator.isMoreMaxLength(password2, 100)){
            password2Error.add("password_too_long_error");
            noErrors = false;
        }
        return noErrors;
    }

    private boolean validateEmail(String email, List<String> emailError, DB manager){
        boolean noErrors = true;
        if (!Validator.isEmailValid(email)){
            emailError.add("email_not_valid");
            noErrors = false;
        }
        if (manager.emailExists(email)){
            emailError.add("email_exists");
            noErrors = false;
        }
        return noErrors;
    }

    private boolean emailDoesntExist(String email, DB manager){
        return !manager.emailExists(email);
    }

    private boolean validateLogin(String login, List<String> loginError, List<String> nonFieldError, DB manager) {
        boolean loginLengthInRange = true;
        if (Validator.isMoreMaxLength(login, 100)){
            loginError.add("login_too_long_error");
            loginLengthInRange = false;
        }
        return loginLengthInRange && isLoginUnique(login, nonFieldError, manager);
    }

    private boolean verifyCaptcha(HttpServletRequest request, List<String> nonFieldErrors) {
        log.debug("Verifying captcha");
        String userResponse = request.getParameter(Constants.USER_RESPONSE);
        log.debug("User response is --> " + userResponse);
        String googleResponse = Connection.getReCAPTCHAResponse(userResponse);
        log.debug("JSON from google --> " + googleResponse);
        JSONParser parser = new JSONParser(googleResponse);
        boolean success = parser.getResult();
        if (success) {
            log.debug("Captcha successful");
            return true;
        } else {
            log.debug("Captcha unsuccessful");
            for (String error : parser.getArray(Constants.CAPTCHA_ERRORS)) {
                log.debug("Setting nonField error to --> " + error);
                nonFieldErrors.add(error);
            }
            return false;
        }
    }

    private boolean isCaptchaClicked(String userResponse) {
        return userResponse != null && !userResponse.trim().isEmpty();
    }
}
