package ua.nure.grankina.periodicals;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Factory for session/request attributes
 *
 * Created by Valeriia on 06.01.2017.
 */
public class SessionAttrsFactory {
    private static Map<String, List<String>> params = new HashMap<>();
    private static Map<String, List<String>> attrs = new HashMap<>();
    private static Logger log = Logger.getLogger(SessionAttrsFactory.class);
    static {populate(); }

    private static void populate(){
        List<String> loginParams = extend(new ArrayList<>(), "login", "password");
        List<String> registerParams = extend(new ArrayList<>(), "login", "password1", "password2", "email");
        List<String> listMessages = extend(new ArrayList<>(), "successMessage", "errorMessage");
        List<String> periodicalParams = extend(new ArrayList<>(), "title", "price", "theme", "period");
        List<String> periodicalsListParams = extend(listMessages, "periodical_title");
        List<String> usersListAttrs = extend(listMessages, "user_login");
        List<String> rechargeParams = extend(new ArrayList<>(), "amount");


        params.put(Path.LOGIN, loginParams);
        params.put(Path.REGISTER, registerParams);
        params.put(Path.ADMIN_LOGIN, loginParams);
        params.put(Path.ADMIN_EDIT_PERIODICAL, periodicalParams);
        params.put(Path.ADMIN_ADD_PERIODICAL, periodicalParams);
        params.put(Path.RECHARGE, rechargeParams);

        attrs.put(Path.LIST_PERIODICALS, periodicalsListParams);
        attrs.put(Path.ADMIN_LIST_PERIODICALS, periodicalsListParams);
        attrs.put(Path.ADMIN_LIST_USERS, usersListAttrs);
    }

    private static List<String> formAttrs(List<String> params){
        List<String> attrs = new ArrayList<>();
        for (String param : params){
            attrs.add(param);
            attrs.add(param + "Error");
        }
        attrs.add("nonFieldErrors");
        return attrs;
    }

    public static List<String> getParams(HttpServletRequest request){
        return getOrDefault(request, params);
    }

    public static List<String> getAtrrs(HttpServletRequest request){
        String path = request.getPathInfo();
        if (params.containsKey(path)) {
            return formAttrs(params.get(path));
        } else {
            return getOrDefault(request, attrs);
        }
    }

    private static List<String> getOrDefault(HttpServletRequest request, Map<String, List<String>> map) {
        if (map.containsKey(request.getPathInfo()))
            return map.get(request.getPathInfo());
        return new ArrayList<>();
    }

    private static List<String> extend(List<String> src, String... values){
        List<String> dest= new ArrayList<>();
        for(String attr : src) {
            dest.add(attr);
        }
        for (String v : values) {
            dest.add(v);
        }
        return dest;
    }

    public static void clear(HttpServletRequest request){
        for (String attr : getAtrrs(request)) {
            log.info(String.format("clearing parameter '%s' --> '%s'", attr, request.getSession().getAttribute(attr)));
            request.getSession().removeAttribute(attr);
        }
    }
}
