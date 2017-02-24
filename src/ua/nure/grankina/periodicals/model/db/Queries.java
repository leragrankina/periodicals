package ua.nure.grankina.periodicals.model.db;

/**
 * Container for queries to DB
 *
 * Created by Valeriia on 10.01.2017.
 */
public class Queries {
    public static final String SELECT_ALL_PERIODICALS = "select * from available_periodicals ";
    public static final String SELECT_ALL_THEMES = "select * from themes";
    public static final String SELECT_ALL_PERIODS = "select * from periods";
    public static final String SELECT_ALL_USERS = "select * from users";
    public static final String SELECT_ALL_ROLES = "select * from roles";
    private static final String SELECT_ALL_SUBSCRIPTIONS = String.format("%s join subscriptions using (periodical_id)"
            , SELECT_ALL_PERIODICALS);
    public static final String SELECT_USER_PERIODICALS = "select * from users_periodicals where user_id = ?";
    public static final String SUBSCRIBE_USER = "insert into subscriptions (user_id, periodical_id) values (?, ?)";
    public static final String INSERT_USER = String.format("insert into users (login, password, email, %s, %s) values (?, ?, ?, ?, ?) "
            , Fields.USER_TIMESTAMP
            , Fields.USER_SALT);
    public static final String DELETE_PERIODICAL = "update periodicals set is_active=false where periodical_id = ?";
    public static final String INSERT_PERIODICAL = String.format("insert into periodicals (%s, %s, %s, %s) values (?, ?, ?, ?) "
            , Fields.PERIODICAL_TITLE
            , Fields.PERIODICAL_PRICE
            , Fields.THEME_ID
            , Fields.PERIOD_ID);
    public static final String EDIT_PERIODICAL = String.format("update periodicals set %s = ?,  %s = ?, %s = ?, %s = ? where %s = ?"
            , Fields.PERIODICAL_TITLE
            , Fields.PERIODICAL_PRICE
            , Fields.THEME_ID
            , Fields.PERIOD_ID
            , Fields.PERIODICAL_ID);
    public static final String FIND_USER_WITH_TOKEN = "select * from users where sha1(concat(email, register_timestamp, salt)) = ? ";
    public static final String UPDATE_TIMESTAMP = String.format("update users set %s = ? where user_id = ?", Fields.USER_TIMESTAMP);
    public static final String INSERT_FB_USER = "insert into users (login, fb_id, blocked, fb_only) values(?, ?, false, true) ";
    public static final String UPDATE_USER_LOGIN = "update users set login=? where user_id=?";
    public static final String SALES_LIST_FOR_GIVEN_MONTH = "select periodical_entities.*, total_count, total_sum from \n" +
            "(select periodical_id, count(subscription_id) as total_count, sum(price) as total_sum\n" +
            "            from subscriptions join periodicals using(periodical_id)\n" +
            "            where month(subscription_date) = ? and year(subscription_date) = ?\n" +
            "            group by periodical_id\n" +
            "            ) AS t\n" +
            "            join periodical_entities using(periodical_id)\n" +
            "            order by total_sum desc";

    /**
     * Appender for other queries for sorting them
     * @param query query to sort
     * @param field field by which to sort
     * @param asc order of sorting
     * @return
     */
    public static String sortQuery(String query, String field, boolean asc){
        String order = asc?"":"DESC";
        return String.format("%s ORDER BY %s %s", query, field, order);
    }

    public static String filterContainsQuery(String query, String field){
        return String.format("%s WHERE %s LIKE ?", query, field);
    }

    public static String filterExactQuery(String query, String field) {
        return String.format("%s WHERE %s = ?", query, field);
    }

    private static String changeUserBalance(double amount, boolean increase){
        String sign = increase?"+":"-";
        return String.format("update users set balance = balance %s " + amount + " where user_id = ?", sign);
    }

    public static String decreaseUserBalance(double amount){
        return changeUserBalance(amount, false);
    }

    public static String increaseUserBalance(double amount){
        return changeUserBalance(amount, true);
    }

    public static String toggleUserBlock(boolean blockToBe){
        return "update users set blocked = " + blockToBe + " where user_id = ?";
    }

    public static String setUserField(String field){
        return String.format("update users set %s = ? where user_id = ?", field);
    }

    public static String filterUsers(String field){
        return String.format("select * from users where %s = ? ", field);
    }

}
