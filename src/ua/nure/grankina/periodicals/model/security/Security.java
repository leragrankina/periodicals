package ua.nure.grankina.periodicals.model.security;

import org.apache.log4j.*;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Random;

/**
 * Class responsible for hashing and encoding hashes
 *
 * Created by Valeriia on 09.01.2017.
 */
public class Security {
    private static Logger log = Logger.getLogger(ua.nure.grankina.periodicals.model.security.Security.class);

    /**
     * Hashes a string and encodes it using Base64 algorithm
     *
     * @param s
     * @return
     */
    public static String hash(String s) {
        byte[] bytes = messageDigest(s);
        return new String(Base64.getEncoder().encode(bytes));
    }

    /**
     * Hashes a string using sha-1
     *
     * @param s
     * @return
     */
    public static byte[] messageDigest(String s){
        byte[] bytes;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            bytes = md.digest(s.getBytes());
            return bytes;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a random array of bits of a given lenght
     *
     * @param len
     * @return
     */
    public static byte[] genRandom(int len) {
        byte[] b = new byte[len];
        Random rand = new Random();
        rand.nextBytes(b);
        return b;
    }

    /**
     * Generates random string of given length
     *
     * @param len
     * @return
     */
    public static String getRandomString(int len){
        byte[] b = Security.genRandom(len);
        byte[] encodedBytes = Base64.getEncoder().encode(b);
        return new String(encodedBytes);
    }

    /**
     * Encodes byte array as hexadecimal numbers
     *
     * @param bytes
     * @return
     */
    public static String hexEncode(byte[] bytes){
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes){
            sb.append(Integer.toHexString((b & 0xf0) >> 4));
            sb.append(Integer.toHexString(b & 0xf));
        }
        return sb.toString();
    }

    /**
     * Hashes a string and encodes it in hexadecimal numbers
     *
     * @param s
     * @return
     */
    public static String hexHash(String s){
        return hexEncode(messageDigest(s));
    }

}
