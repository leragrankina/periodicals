package ua.nure.grankina.periodicals.web;

import org.apache.log4j.*;
import ua.nure.grankina.periodicals.Attributes;
import ua.nure.grankina.periodicals.Path;
import ua.nure.grankina.periodicals.model.db.DB;
import ua.nure.grankina.periodicals.model.db.entity.Sale;
import ua.nure.grankina.periodicals.model.security.Validator;
import ua.nure.grankina.periodicals.web.captcha.Constants;
import ua.nure.grankina.periodicals.web.report.SalesReport;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

/**
 * Created by USER-PC on 21.01.2017.
 */
public class ServeDocument extends HttpServlet{
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.ServeDocument.class);
    {log.setLevel(Level.INFO);}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String m= req.getParameter("month");
        String y= req.getParameter("year");
        String lang = req.getParameter("lang");
        if (isInputInvalid(m, y, lang)){
            if (isLanguageUnknown(lang))
                bindForm(req, m, y, "unknown_lang");
            else{
                bindForm(req, m, y, "date_invalid");
            }
            resp.sendRedirect(Path.redirectTo(Path.ADMIN_LIST_PERIODICALS));
        } else {
            ServletContext ctx = req.getServletContext();
            int month = Integer.parseInt(m);
            int year = Integer.parseInt(y);
            String filePath = generateFileName(month, year, lang);
            String absolutePath = ctx.getRealPath(filePath);
            log.debug("absolute path --> " + absolutePath);
            if (ifFileNotExists(absolutePath) || isReportForCurrentMonth(month, year)) {
                DB manager = (DB)ctx.getAttribute(Attributes.DB_MANAGER);
                List<Sale> sales = manager.findSalesForGivenMonth(month, year);
                generateSalesReport(absolutePath, sales, lang, month, year);
                log.debug("File not exists --> " + ifFileNotExists(absolutePath));
            }
            clearForm(req);
            sendBytes(req, resp, absolutePath);
        }
    }

    private boolean isReportForCurrentMonth(int month, int year) {
        return month == getCurrentMonth() && year == getCurrentYear();
    }

    private void generateSalesReport(String absolutePath, List<Sale> sales, String lang, int month, int year) {
        log.debug("generating a report...");
        SalesReport report = new SalesReport();
        report.setSales(sales);
        report.setLang(lang);
        report.setMonth(month);
        report.setYear(year);
        report.writeSales(absolutePath);
    }

    private boolean ifFileNotExists(String absolutePath) {
        File file = new File(absolutePath);
        if (file.exists()){
            log.debug(String.format("file '%s' exists", absolutePath));
            return false;
        } else{
            log.debug(String.format("file '%s' doesn't exists", absolutePath));
            return true;
        }
    }

    private String generateFileName(int month, int year, String lang) {
        return String.format("/WEB-INF/reports/%d-%d-%s.pdf", month, year, lang);
    }

    private void bindForm(HttpServletRequest req, String month, String year, String error) {
        req.getSession().setAttribute("nonFieldErrors", error);
        req.getSession().setAttribute("month", month);
        req.getSession().setAttribute("year", year);
    }

    private void clearForm(HttpServletRequest req) {
        req.getSession().removeAttribute("month");
        req.getSession().removeAttribute("year");
        log.debug("form is cleared, eg session.month --> " + req.getSession().getAttribute("month"));
    }

    private boolean isInputInvalid(String month, String year, String lang){
        return isMonthInvalid(month) || isYearInvalid(year) || isMonthInTheFuture(month, year) || isLanguageUnknown(lang);
    }

    private boolean isLanguageUnknown(String lang){
        if (lang.equals("ru") || lang.equals("en")){
            log.debug("lang alright");
            return false;
        } else{
            log.debug("unknown language --> " + lang);
            return true;
        }
    }

    private boolean isMonthInvalid(String month){
        if (Validator.isNotNumeric(month)) {
            log.debug("month is not numeric");
            return true;
        }
        else {
            int m = Integer.parseInt(month);
            if (1 <= m && m <= 12) {
                return false;
            } else {
                log.debug(" not 1 <= month <= 12");
                return true;
            }
        }
    }

    private boolean isYearInvalid(String year){
        if (Validator.isNotNumeric(year)){
            log.debug("year is not numeric");
            return true;
        } else{
            int y= Integer.parseInt(year);
            if (Constants.REPORT_START_YEAR <= y && y <= getCurrentYear()){
                return false;
            } else {
                log.debug("year is not 2017 <= year <= 2017");
                return true;
            }
        }
    }

    private int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    private int getCurrentMonth(){
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    private boolean isMonthInTheFuture(String m, String y){
        int month = Integer.parseInt(m);
        int year = Integer.parseInt(y);
        if (year == getCurrentYear() && month > getCurrentMonth()){
            log.debug(String.format("current year --> %s & current month --> %s", getCurrentYear(), getCurrentMonth()));
            log.debug("month is set in the future");
            return true;
        }
        return false;
    }

    private void sendBytes(HttpServletRequest req, HttpServletResponse resp, String absolutePath) throws ServletException, IOException{
        setResponseHeaders(resp, new File(absolutePath).getName());
        ServletContext ctx = req.getServletContext();
        InputStream fileInputStream = new FileInputStream(absolutePath);
        ServletOutputStream out = resp.getOutputStream();
        cat(fileInputStream, out);
    }

    private void setResponseHeaders(HttpServletResponse resp, String fileName) {
        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", String.format("attachment;filename=%s", fileName));
    }

    private void cat(InputStream input, ServletOutputStream output) throws IOException {
        byte[] outputByte = new byte[4096];
        while(input.read(outputByte, 0, 4096) != -1){
            output.write(outputByte, 0, 4096);
        }
        input.close();
        output.flush();
        output.close();
    }
}
