package ua.nure.grankina.periodicals.web.strategy;

import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.SessionAttrsFactory;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.security.Validator;
import ua.nure.grankina.periodicals.model.db.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Action for recharging user's balance
 *
 * Created by Valeriia on 04.01.2017.
 */
public class RechargeAction implements Action{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = (User) request.getSession().getAttribute("user");
        String amountStr = request.getParameter("amount");
        DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);
        if (Validator.isNumeric(amountStr)){
            double amount = Double.valueOf(amountStr);
            if (Validator.isValueInRange(amount, 0, 10000)){
                SessionAttrsFactory.clear(request);
                manager.increaseBalance(currentUser, amount);
                return Path.redirectTo(Path.PROFILE);
            }
            else{
                request.getSession().setAttribute("amountError", Attributes.ERR_NOT_IN_RANGE);
            }
        }
        else{
            request.getSession().setAttribute("amountError", Attributes.ERR_NUMBERS_ONLY);
        }
        return Path.redirectTo(Path.RECHARGE);
    }
}
