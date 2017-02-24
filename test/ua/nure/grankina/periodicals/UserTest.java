package ua.nure.grankina.periodicals;


import org.junit.Before;
import org.junit.Test;
import ua.nure.grankina.periodicals.model.db.entity.Periodical;
import ua.nure.grankina.periodicals.model.db.entity.Role;
import ua.nure.grankina.periodicals.model.db.entity.User;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by USER-PC on 18.01.2017.
 */
public class UserTest {
    private User user;
    @Before
    public void setUp(){
        user = new User();
    }

    @Test
    public void idTest(){
        user.setId(1);
        assertEquals(1L, user.getId().longValue());
    }

    @Test
    public void passwordTest(){
        user.setPassword("pass");
        assertEquals("pass", user.getPassword());
    }

    @Test
    public void loginTest(){
        user.setLogin("user");
        assertEquals("user", user.getLogin());
    }

    @Test
    public void periodicalsTest(){
        user.setPeriodicals(new ArrayList<Periodical>());
        assertEquals(new ArrayList<Periodical>(), user.getPeriodicals());
    }

    @Test
    public void testBalance(){
        user.setBalance(1.);
        assertEquals(new Double(1), user.getBalance());
    }

    @Test
    public void testRole(){
        user.setRole(Role.CLIENT);
        assertEquals(Role.CLIENT, user.getRole());
    }

    @Test
    public void blockedTest(){
        user.setBlocked(true);
        assertEquals(true, user.isBlocked());
    }

    @Test
    public void langTest(){
        user.setLang("en");
        assertEquals("en", user.getLang());
    }

    @Test
    public void tokenTest(){
        user.setTokenHash("hash");
        assertEquals("hash", user.getTokenHash());
    }

    @Test
    public void timestampTest(){
        user.setTimestamp(1000);
        assertEquals(1000, user.getTimestamp());
    }

    @Test
    public void saltTest(){
        user.setSalt("salt");
        assertEquals("salt", user.getSalt());
    }

    @Test
    public void emailTest(){
        user.setEmail("user@com");
        assertEquals("user@com", user.getEmail());
    }
}
