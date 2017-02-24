package ua.nure.grankina.periodicals;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.web.captcha.Constants;

import javax.servlet.http.HttpServletRequest;

/**
 * Class for aggregation site paths
 *
 * Created by Valeriia on 06.01.2017.
 */
public class Path {
    private static Logger log = Logger.getLogger(ua.nure.grankina.periodicals.Path.class);
    //Admin links
    public static final String ADMIN_LOGIN = "/admin/login";
    public static final String ADMIN_LIST_PERIODICALS = "/admin/periodicals";
    public static final String ADMIN_ADD_PERIODICAL = "/admin/periodicals/add";
    public static final String ADMIN_EDIT_PERIODICAL = "/admin/periodicals/edit";
    public static final String ADMIN_DELETE_PERIODICAL = "/admin/periodicals/delete";
    public static final String ADMIN_LIST_USERS = "/admin/users";
    public static final String ADMIN_TOGGLE_USER = "/admin/toggle";
    public static final String ADMIN_REPORT = "/admin/report";

    //Available to all
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String LIST_PERIODICALS = "/periodicals";
    public static final String LOGOUT = "/logout";
    public static final String SUCCESS = "/success";
    public static final String CHANGE_LOCALE = "/change_locale";
    public static final String BLOCKED = "/block";
    public static final String ERROR = "/error";
    public static final String CONFIRM = "/confirm";
    public static final String EXPIRED = "/linkExpired";
    public static final String ACTIVATE = "/activate";
    public static final String FB_LOGIN = "/fblogin";
    public static final String FULL_PATH_FB_LOGIN = String.format("http://localhost:8081/pages%s", FB_LOGIN);
    public static final String FB_SEND_FOR_CODE = String.format("https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s&response_type=code", Constants.FB_CLIENT_ID, FULL_PATH_FB_LOGIN);

    //Authorized links
    public static final String PROFILE = "/protected/profile";
    public static final String SUBSCRIBE = "/protected/subscribe";
    public static final String RECHARGE = "/protected/recharge";

    public static final String CONTROLLER = "/pages";

    public static String redirectTo(String forward){
        return CONTROLLER + forward;
    }

    public static String forwardTo(String forward){
        return forward;
    }

    public static String getFullURI(HttpServletRequest request){
        log.debug("Finding full URI for request --> " + request.getPathInfo());
        String query = request.getQueryString();
        String uri = request.getPathInfo() + (query == null ? "" : "?" + query);
        if (request.getPathInfo().equals(ADMIN_EDIT_PERIODICAL) && request.getMethod().equals("POST")) {
            uri= request.getPathInfo() + "?id=" + request.getParameter("id");
        }
        log.debug("fullURI --> " + uri);
        return Path.redirectTo(uri);
    }

    public static String sendForToken(String code){
        return String.format("https://graph.facebook.com/v2.8/oauth/access_token?client_id=%s&redirect_uri=%s&client_secret=%s&code=%s"
                , Constants.FB_CLIENT_ID
                , FULL_PATH_FB_LOGIN
                , Constants.FB_CLIENT_SECRET
                , code);
    }

    public static String sendForUserInfo(String token){
        return "https://graph.facebook.com/me?access_token=" + token;
    }

}
