package ua.nure.grankina.periodicals.web.strategy.admin.periodicals;

import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.SessionAttrsFactory;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.security.Validator;
import ua.nure.grankina.periodicals.model.db.entity.Period;
import ua.nure.grankina.periodicals.model.db.entity.Theme;
import ua.nure.grankina.periodicals.web.strategy.Action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Edits or adds a periodical
 *
 * Created by Valeriia on 05.01.2017.
 */
public class AditPeriodicalAction implements Action {
    private List<String> validate(String theme, String period){
        List<String> nonFieldErrors = new ArrayList<>();
        if (!Validator.containsDigitsOnly(theme)){
            nonFieldErrors.add("theme_id_error");
        }
        if (!Validator.containsDigitsOnly(period)){
            nonFieldErrors.add("period_id_error");
        }
        return nonFieldErrors;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = Path.getFullURI(request);
        String id = request.getParameter("id");
        boolean editing = isEditing(id);
        List<String> nonFieldErrors = new ArrayList<>();
        String title = request.getParameter("title");
        String theme = request.getParameter("theme");
        String period = request.getParameter("period");
        String priceValue = request.getParameter("price");
        List<String> validationErrors = validate(theme, period);
        if (validationErrors.size() > 0){
            request.getSession().setAttribute("nonFieldErrors", validationErrors);
            return forward;
        }
        if (Validator.isNotNumeric(priceValue)){
            request.getSession().setAttribute("priceError", "numbers_only_error");
            request.getSession().setAttribute("nonFieldErrors", nonFieldErrors);
            return forward;
        } else {
            double price = Double.valueOf(priceValue);
            if (Validator.isLessMinValue(price, 0)) {
                request.getSession().setAttribute("priceError", "price_negative_error");
                return forward;
            }
            if (Validator.isMoreMaxValue(price, 10000)){
                request.getSession().setAttribute("priceError", "price_too_big_error");
                return forward;
            }

            DB manager = (DB) request.getServletContext().getAttribute(Attributes.DB_MANAGER);

            Theme t = manager.findTheme(Long.valueOf(theme));
            Period per = manager.findPeriod(Long.valueOf(period));
            String successMessage;
            if (editing) {
                successMessage = "periodical_edit_success";
                manager.editPeriodical(Long.valueOf(id), title, t, price, per);
            } else {
                successMessage = "periodical_add_success";
                manager.insertPeriodical(title, t, price, per);
            }
            request.getSession().setAttribute("periodical_title", title);
            request.getSession().setAttribute("successMessage", successMessage);
            SessionAttrsFactory.clear(request);
            return Path.redirectTo(Path.ADMIN_LIST_PERIODICALS);
        }
    }

    private boolean isEditing(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return true;
    }
}
