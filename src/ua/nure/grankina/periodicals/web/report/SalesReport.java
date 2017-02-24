package ua.nure.grankina.periodicals.web.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.model.db.entity.Sale;

/**
 * Created by USER-PC on 21.01.2017.
 */
public class SalesReport {
    private List<Sale> sales = new ArrayList<Sale>();
    private String lang = "en";
    private ResourceBundle bundle = ResourceBundle.getBundle("resources", new Locale(lang));
    private int month;
    private int year;
    private Logger log = Logger.getLogger(SalesReport.class);
    private String fontPath = "C:\\Users\\USER-PC\\IdeaProjects\\periodicals\\out\\artifacts\\SummaryTask4_war_exploded\\WEB-INF\\reports\\arial.ttf";
    private Font font = FontFactory.getFont(fontPath, "Cp1251", BaseFont.EMBEDDED);
    {log.setLevel(Level.DEBUG);}

    public void setSales(List<Sale> sales){
        this.sales = sales;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
        bundle = ResourceBundle.getBundle("resources", new Locale(lang));
        log.debug("bundle locale is --> " + bundle.getLocale());
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void writeSales(String filepath){
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            File file = createFileIfNotExists(filepath);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            log.debug("Setting header...");
            Paragraph header = new Paragraph(MessageFormat.format(bundle.getString("report_header"), month, year), font);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            if (sales.size() > 0) {
                PdfPTable t = writeTable();
                log.debug("Adding table...");
                document.add(t);
            } else{
                log.debug("Adding 'none sales' paragraph");
                Paragraph p = new Paragraph(bundle.getString("no_sales"), font);
                p.setAlignment(Element.ALIGN_JUSTIFIED);
            }
            document.close();
        } catch (DocumentException | IOException e){
            log.error(e.getMessage());
            throw new RuntimeException("Cannot write pdf");
        }
    }

    private PdfPTable writeTable(){
        PdfPTable t = new PdfPTable(4);
        t.setSpacingBefore(25);
        t.setSpacingAfter(25);
        setTableHeaders(t);
        for (int i = 0; i < sales.size(); i++){
            Sale s = sales.get(i);
            t.addCell(Integer.toString(i + 1));
            t.addCell(new Phrase(s.getPeriodical().getTitle(), font));
            t.addCell(Integer.toString(s.getCount()));
            t.addCell(Double.toString(s.getSum()));
        }
        return t;
    }

    private void setTableHeaders(PdfPTable t) {
        log.debug("Setting table headers");
        PdfPCell c1 = new PdfPCell(new Phrase(bundle.getString("periodicals_jsp.table.id"), font));
        t.addCell(c1);
        PdfPCell c2 = new PdfPCell(new Phrase(bundle.getString("periodicals_jsp.table.title"), font));
        t.addCell(c2);
        PdfPCell c3 = new PdfPCell(new Phrase(bundle.getString("num_of_users"), font));
        t.addCell(c3);
        PdfPCell c4 = new PdfPCell(new Phrase(bundle.getString("total_sum"), font));
        t.addCell(c4);
    }

    private File createFileIfNotExists(String filepath) throws IOException {
        try {
            Files.createFile(Paths.get(filepath));
            log.debug("File created!");
        } catch (FileAlreadyExistsException e){
            log.debug(String.format("File %s not exists. Creating one...", filepath));
        }
        return new File(filepath);
    }
}
