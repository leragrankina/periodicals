package ua.nure.grankina.periodicals.web.net;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.model.exception.Messages;
import ua.nure.grankina.periodicals.web.captcha.Constants;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by USER-PC on 16.01.2017.
 */
public class Connection {
    private static Logger log = Logger.getLogger(ua.nure.grankina.periodicals.web.net.Connection.class);

    public static String sendPost(String target, String urlParameters) {
        HttpURLConnection connection = getConnection(target, urlParameters, "POST");
        writeParameters(urlParameters, connection);
        String response = getResponse(connection);
        return response;
    }

    public static String sendGet(String target){
        HttpURLConnection connection = getConnection(target, "", "GET");
        String response = getResponse(connection);
        return response;
    }

    private static String getResponse(HttpURLConnection connection) {
        try {
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuffer response = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append(System.lineSeparator());
            }
            rd.close();
            return response.toString();
        } catch (IOException e){
            log.error(e.getMessage());
            throw new RuntimeException("Cannot obtain response");
        }
    }

    private static void writeParameters(String urlParameters, HttpURLConnection connection) {
        try {
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();
        } catch (IOException e){
            log.error(e.getMessage());
            throw new RuntimeException("Cannot write post parameters");
        }
    }

    private static HttpURLConnection getConnection(String target, String urlParameters, String method) {
        try {
            URL url = new URL(target);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            return connection;
        } catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("Cannot obtain connection");
        }
    }

    public static String getReCAPTCHAResponse(String userResponse){
        try {
            String urlParameters = String.format("secret=%s&response=%s", Constants.SECRET_KEY, userResponse);
            String url = Constants.CAPTCHA_VERIFICATION;
            return sendPost(url, urlParameters);
        } catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_SEND_REQUEST);
        }
    }

}
