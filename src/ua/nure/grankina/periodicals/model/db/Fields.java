package ua.nure.grankina.periodicals.model.db;

/**
 * DRY implementation for fields
 *
 * Created by Valeriia on 10.01.2017.
 */
public class Fields {
    //Periodical fields
    public static final String PERIODICAL_ID = "periodical_id";
    public static final String PERIODICAL_TITLE = "title";
    public static final String PERIODICAL_PRICE = "price";

    //Theme fields
    public static final String THEME_ID = "theme_id";
    public static final String THEME_NAME = "theme_name";

    //Period fields
    public static final String PERIOD_ID = "period_id";
    public static final String PERIOD_NAME = "period_name";

    //Role fields
    public static final String ROLE_ID = "role_id";
    public static final String ROLE_NAME = "role_name";

    //User fields
    public static final String USER_ID = "user_id";
    public static final String USER_LOGIN = "login";
    public static final String USER_PASSWORD = "password";
    public static final String USER_BLOCKED = "blocked";
    public static final String USER_BALANCE = "balance";
    public static final String USER_TOKEN_HASH = "token_hash";
    public static final String USER_LANG = "lang";
    public static final String USER_EMAIL = "email";
    public static final String USER_TIMESTAMP = "register_timestamp";
    public static final String USER_SALT = "salt";
    public static final String FB_ID = "fb_id";
    public static final String FB_ONLY = "fb_only";
    public static final String TOTAL_COUNT = "total_count";
    public static final String TOTAL_SUM = "total_sum";
}
