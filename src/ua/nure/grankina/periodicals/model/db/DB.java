package ua.nure.grankina.periodicals.model.db;

import ua.nure.grankina.periodicals.model.db.entity.*;

import java.util.List;

/**
 * Created by USER-PC on 19.01.2017.
 */
public interface DB {
    User findUser(long id);
    
    User findUser(String login);
    
    User findUserByCookieToken(String token);
    
    void setUserTokenHash(User user, String TokenHash);
    
    void updateUser(User user);
    
    void setUserLocale(User currentUser, String locale);
    
    void insertUser(String login, String password2, String email, long timestamp, String salt);

    void subscribeUser(User currentUser, Periodical periodical);

    Periodical findPeriodical(long along);

    List<User> findClients();

    void unblockUser(User user);

    void blockUser(User user);

    Theme findTheme(long along);

    Period findPeriod(long along);

    void editPeriodical(long along, String title, Theme t, double price, Period per);

    void insertPeriodical(String title, Theme t, double price, Period per);

    void deletePeriodical(long id);

    List<Periodical> findPeriodicals();

    List<Period> findPeriods();

    List<Theme> findThemes();

    User findUserByConfirmationToken(String token);

    void updateTimestamp(User user);

    List<Periodical> filterPeriodicalsBy(String theme, String themeValue);

    List<Periodical> sortPeriodicalsBy(String sortParam, String orderParam);

    void increaseBalance(User currentUser, double amount);

    boolean emailExists(String email);

    boolean userExists(String login);

    User findUserByFBId(long id);

    void insertFBUser(long id, String name);

    void updateUserLogin(long id, String newLogin);

    List<Sale> findSalesForGivenMonth(int month, int year);

}
