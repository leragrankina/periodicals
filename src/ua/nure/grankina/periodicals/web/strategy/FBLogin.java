package ua.nure.grankina.periodicals.web.strategy;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.db.entity.User;
import ua.nure.grankina.periodicals.web.Authentication;
import ua.nure.grankina.periodicals.web.captcha.Constants;
import ua.nure.grankina.periodicals.web.json.JSONParser;
import ua.nure.grankina.periodicals.web.net.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by USER-PC on 20.01.2017.
 */
public class FBLogin implements Action {
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.strategy.FBLogin.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (userNotLoggedInWithFB(request)){
            return sendToFBLoginPage();
        } else if(request.getParameter("error") != null){
            request.getSession().setAttribute("nonFieldErrors", request.getParameter("error"));
            return Path.redirectTo(Path.LOGIN);
        }
        else if (FBHasSentCode(request)){
            String token = getAccessTokenFromFB(request);
            String FBInfo = getUserInfoFromFB(token);
            authenticateUser(request, response, FBInfo);
            return Path.redirectTo(Path.LIST_PERIODICALS);
        }
        return Path.redirectTo(Path.LIST_PERIODICALS);
    }

    private String getUserInfoFromFB(String token) {
        String response = Connection.sendGet(Path.sendForUserInfo(token));
        log.debug("Response from FB --> " + response);
        return response;
    }

    private String getAccessTokenFromFB(HttpServletRequest request) {
        String code = request.getParameter("code");
        String FBResponse = Connection.sendGet(Path.sendForToken(code));
        return new JSONParser(FBResponse).getString("access_token");
    }

    private void authenticateUser(HttpServletRequest request, HttpServletResponse response, String FBInfo) {
        JSONParser info = new JSONParser(FBInfo);
        long fbId = info.getLong(Constants.FB_ID);
        String name = info.getString(Constants.FB_NAME);
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        registerIfNotFound(fbId, name, manager);
        User user = fetchFBUser(fbId, manager);
        updateNameIfChanged(name, manager, user);
        Authentication.authenticateUser(request, response, user);
    }

    private void updateNameIfChanged(String name, DB manager, User user) {
        if (!user.getLogin().equals(name)){
            user.setLogin(name);
            manager.updateUserLogin(user.getId(), name);
        }
    }

    private void registerIfNotFound(long fbId, String name, DB manager) {
        if (userNotRegistered(fbId, manager)){
            register(fbId, name, manager);
        }
    }

    private User fetchFBUser(long fbId, DB manager) {
        return manager.findUserByFBId(fbId);
    }

    private void register(long id, String name, DB manager) {
        manager.insertFBUser(id, name);
    }

    private boolean userNotRegistered(long fbId, DB manager) {
        return manager.findUserByFBId(fbId) == null;
    }

    private boolean FBHasSentCode(HttpServletRequest request) {
        return request.getParameter("code") != null;
    }

    private String sendToFBLoginPage() {
        return Path.FB_SEND_FOR_CODE;
    }

    private boolean userNotLoggedInWithFB(HttpServletRequest request) {
        return request.getParameterMap().size() == 0;
    }
}
