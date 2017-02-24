package ua.nure.grankina.periodicals.web.utils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Valeriia on 18.01.2017.
 */
public class CurrentYearTag extends SimpleTagSupport {
    public void doTag() throws JspException, IOException{
        String currentYear = new SimpleDateFormat("yyyy").format(new Date());
        getJspContext().getOut().print(currentYear);
    }
}
