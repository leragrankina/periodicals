package ua.nure.grankina.periodicals.web.strategy;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.web.strategy.admin.periodicals.AditPeriodicalAction;
import ua.nure.grankina.periodicals.web.strategy.admin.periodicals.DeletePeriodicalAction;
import ua.nure.grankina.periodicals.web.strategy.admin.periodicals.ReadPeriodicalAction;
import ua.nure.grankina.periodicals.web.strategy.admin.users.ListUsersAction;
import ua.nure.grankina.periodicals.web.strategy.admin.users.ToggleUserAction;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for strategies based on path info
 *
 * Created by Valeriia on 02.01.2017.
 */
public class ActionFactory {
    private static Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.strategy.ActionFactory.class);
    private static Map<String, Action> actions = new HashMap<>();

    static{
        actions.put("GET" + Path.LIST_PERIODICALS, new ua.nure.grankina.periodicals.web.strategy.ListPeriodicalsAction());
        actions.put("GET" + Path.LOGOUT, new LogoutAction());
        actions.put("GET" + Path.PROFILE, new ProfileAction());
        actions.put("GET" + Path.CONFIRM, new FinishRegistrationAction());
        actions.put("GET" + Path.REGISTER, new SetPublicKey());
        actions.put("GET" + Path.FB_LOGIN, new FBLogin());
        actions.put("POST" + Path.CHANGE_LOCALE, new ChangeLocaleAction());

        actions.put("POST" + Path.LOGIN, new LoginAction());
        actions.put("POST" + Path.SUBSCRIBE, new SubscribeAction());
        actions.put("POST" + Path.RECHARGE, new RechargeAction());
        actions.put("POST" + Path.REGISTER, new RegisterAction());

        actions.put("GET/admin", new PageRedirectAction(Path.redirectTo(Path.ADMIN_LIST_PERIODICALS)));

        actions.put("GET" + Path.ADMIN_LIST_PERIODICALS, new ua.nure.grankina.periodicals.web.strategy.ListPeriodicalsAction());
        actions.put("GET" + Path.ADMIN_ADD_PERIODICAL, new ReadPeriodicalAction());
        actions.put("GET" + Path.ADMIN_EDIT_PERIODICAL, new ReadPeriodicalAction());
        actions.put("GET" + Path.ADMIN_LIST_USERS, new ListUsersAction());

        actions.put("POST" + Path.ADMIN_LOGIN, new AdminLoginAction());
        actions.put("POST" + Path.ADMIN_DELETE_PERIODICAL, new DeletePeriodicalAction());
        actions.put("POST" + Path.ADMIN_ADD_PERIODICAL, new AditPeriodicalAction());
        actions.put("POST" + Path.ADMIN_EDIT_PERIODICAL, new AditPeriodicalAction());
        actions.put("POST" + Path.ADMIN_TOGGLE_USER, new ToggleUserAction());
    }

    public static Action getAction(HttpServletRequest request){
        String key = request.getMethod() + request.getPathInfo();
        Action action = actions.containsKey(key)? actions.get(key) : new NoAction();
        log.info("Passing request to --> " + action.getClass());
        return action;
    }
}
