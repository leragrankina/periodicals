package ua.nure.grankina.periodicals.model.mail;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.model.exception.Messages;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Email logic goes here
 *
 * Created by Valeriia on 15.01.2017.
 */
public class Email {
    private static Logger log = Logger.getLogger(ua.nure.grankina.periodicals.model.mail.Email.class);

    private static void close(Transport t){
        if (t != null){
            try{
                t.close();
            } catch (MessagingException e){

            }
        }
    }

    private static void send(String message , String toAddress , String fromAddress , String subject , String username , String password){
    try {
            Properties props = new Properties();
            props.setProperty("mail.mime.charset", "UTF-8");
            final Session session = Session.getInstance(props);
            final Message msg = new MimeMessage(session);
            Address to = new InternetAddress(toAddress);
            Address from = new InternetAddress(fromAddress);

            msg.setContent(message, "text/html; charset=UTF-8");
            msg.setFrom(from);
            msg.setRecipient(Message.RecipientType.TO, to);
            msg.setSubject(subject);

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    Transport t = null;
                    try{
                        t = session.getTransport("smtps");
                        t.connect("smtp.gmail.com", username, password);
                        t.sendMessage(msg, msg.getAllRecipients());
                    } catch (MessagingException ex){
                        log.error(ex.getMessage());
                        throw new RuntimeException(Messages.ERR_CANNOT_SEND_EMAIL, ex);
                    } finally {
                        close(t);
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();
        } catch (MessagingException e){
            log.debug(e.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_SEND_EMAIL);
        }

    }

    public static void sendVerification(String address, String confirmationToken, String lang){
        String confirmationLink = "http://localhost:8081/pages/confirm?token=" + confirmationToken;
        ResourceBundle bundle = ResourceBundle.getBundle("resources", new Locale(lang));
        log.debug("Email message --> " + bundle.getString("email_thanx"));
        String message = String.format("<p>%s</p><p><a href=\"%s\" style=\"font-size:8pt;\">%s</a></p>"
                , bundle.getString("email_thanx")
                , confirmationLink
                , confirmationLink
        );
        send(message, address, "periodicals"
                , "Test"
                , "summarytask4.grankina@gmail.com"
                , "summaryTask4");
    }

    public static boolean isEmailValid(String email){
        try {
            new InternetAddress(email).validate();
            log.debug(String.format("email %s is valid", email));
            return true;
        } catch (AddressException e){
            log.debug(String.format("email %s is invalid", email));
            return false;
        }
    }
}
