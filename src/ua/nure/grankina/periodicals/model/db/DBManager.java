package ua.nure.grankina.periodicals.model.db;

import org.apache.log4j.Logger;
import ua.nure.grankina.periodicals.model.db.entity.*;
import ua.nure.grankina.periodicals.model.exception.Messages;
import ua.nure.grankina.periodicals.model.security.Security;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.*;
import java.util.*;

/**
 * Class for talking to DB
 *
 * Created by Valeriia on 01.01.2017.
 */
public class DBManager implements DB {
    private static DBManager instance;
    private Logger log = Logger.getLogger(ua.nure.grankina.periodicals.model.db.DBManager.class);
    private javax.sql.DataSource ds;

    private Connection getConnection() {
        Connection con;
        try {
            con = ds.getConnection();
        } catch (SQLException ex) {
            log.error("Cannot obtain connection", ex);
            throw new RuntimeException(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
        }
        return con;
    }

    private void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                log.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, ex);
            }
        }
    }

    private void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                log.error(Messages.ERR_CANNOT_CLOSE_STATEMENT, ex);
            }
        }
    }

    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                log.error(Messages.ERR_CANNOT_CLOSE_RESULTSET, ex);
            }
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        close(rs);
        close(con, stmt);
    }

    private void close(Connection con, Statement stmt){
        close(stmt);
        close(con);
    }

    private void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                log.error("Cannot rollback transaction", ex);
            }
        }
    }

    private DBManager(){
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (javax.sql.DataSource) envContext.lookup("jdbc/ST4DB");
            log.trace("Data source ==> " + ds);
        } catch (NamingException ex) {
            log.error("Cannot obtain DataSource --> ", ex);
            throw new RuntimeException(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
        }

    }

    public synchronized static DBManager getInstance(){
        if (instance == null){
            instance = new DBManager();
        }
        return instance;
    }

    /////////////////////////////////////////////
    //     Methods related to Periodicals      //
    ////////////////////////////////////////////

    public List<Periodical> findPeriodicals() {
        List<Periodical> list = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(Queries.SELECT_ALL_PERIODICALS);
            while (rs.next()) {
                list.add(extractPeriodical(rs));
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_EXTRACT_PERIODICAL, ex);
        } finally {
            close(con, stmt, rs);
        }
        return list;
    }

    public Periodical findPeriodical(long periodicalId){
        Periodical p = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.filterExactQuery(Queries.SELECT_ALL_PERIODICALS, Fields.PERIODICAL_ID));
            stmt.setLong(1, periodicalId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                p = extractPeriodical(rs);
            }
            if (rs.next()){
                throw new SQLException(Messages.ERR_DATA_INTEGRITY);
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_FIND_PERIODICAL, ex);
        } finally {
            close(con, stmt, rs);
        }
        return p;
    }

    private String periodicalFieldFactory(String field) throws IllegalArgumentException{
        switch (field){
            case "id":
                return Fields.PERIODICAL_ID;
            case "title":
                return Fields.PERIODICAL_TITLE;
            case "price":
                return Fields.PERIODICAL_PRICE;
            case "theme":
                return Fields.THEME_NAME;
            default:
                throw new IllegalArgumentException();
        }
    }

    public List<Periodical> sortPeriodicalsBy(String field, String order){
        List<Periodical> list = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();

            String orderField = periodicalFieldFactory(field);
            stmt = con.createStatement();
            rs = stmt.executeQuery(Queries.sortQuery(Queries.SELECT_ALL_PERIODICALS, orderField, "a".equals(order)));
            while (rs.next()) {
                list.add(extractPeriodical(rs));
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_SORT_PERIODICALS, ex);
        } catch (IllegalArgumentException e){
            return new ArrayList<Periodical>();
        } finally {
            close(con, stmt, rs);
        }
        return list;
    }

    public List<Periodical> filterPeriodicalsBy(String field, String value){
        String predicateField = periodicalFieldFactory(field);
        List<Periodical> list = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            String query;
            if (predicateField.equals(Fields.PERIODICAL_TITLE)){
                query = Queries.filterContainsQuery(Queries.SELECT_ALL_PERIODICALS, predicateField);
                value = "%" + value + "%";
            } else {
                query = Queries.filterExactQuery(Queries.SELECT_ALL_PERIODICALS, predicateField);
            }
            stmt = con.prepareStatement(query);
            stmt.setString(1, value);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractPeriodical(rs));
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_FILTER_PERIODICALS, ex);
        } catch (IllegalArgumentException e){
            return new ArrayList<Periodical>();
        } finally {
            close(con, stmt, rs);
        }
        return list;
    }

    public void insertPeriodical(String title, Theme theme, double price, Period period){
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.INSERT_PERIODICAL);
            stmt.setString(1, title);
            stmt.setDouble(2, price);
            stmt.setLong(3, theme.getId());
            stmt.setLong(4, period.getId());
            int rowCount = stmt.executeUpdate();
            if (rowCount != 1){
                throw new SQLException("Row count not equal to 1 --> " + rowCount);
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_INSERT_PERIODICAL, ex);
        } finally {
            close(con, stmt);
        }

    }

    public void editPeriodical(long id, String title, Theme theme, double price, Period period){
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.EDIT_PERIODICAL);
            stmt.setString(1, title);
            stmt.setDouble(2, price);
            stmt.setLong(3, theme.getId());
            stmt.setLong(4, period.getId());
            stmt.setLong(5, id);
            int rowCount = stmt.executeUpdate();
            if (rowCount != 1){
                throw new SQLException("Row count not equal to 1 --> " + rowCount);
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_EDIT_PERIODICAL, ex);
        } finally {
            close(con, stmt);
        }

    }

    public void deletePeriodical(long id){
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.DELETE_PERIODICAL);
            stmt.setLong(1, id);
            int rowCount = stmt.executeUpdate();
            if (rowCount != 1){
                throw new SQLException("Row count not equal to 1 --> " + rowCount);
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_DELETE_PERIODICAL, ex);
        } finally {
            close(con, stmt);
        }
    }

    //////////////////////////////////////////////
    //        Methods related to Themes         //
    //////////////////////////////////////////////

    public List<Theme> findThemes(){
        List<Theme> list = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(Queries.SELECT_ALL_THEMES);
            while (rs.next()) {
                list.add(extractTheme(rs));
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_EXTRACT_THEME, ex);
        } finally {
            close(con, stmt, rs);
        }
        return list;
    }

    public Theme findTheme(long id){
        Theme theme = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.filterExactQuery(Queries.SELECT_ALL_THEMES, Fields.THEME_ID));
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                theme = extractTheme(rs);
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_EXTRACT_THEME, ex);
        } finally {
            close(con, stmt, rs);
        }
        return theme;
    }


    //////////////////////////////////////////////
    //        Methods related to Periods        //
    //////////////////////////////////////////////

    public List<Period> findPeriods(){
        List<Period> list = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(Queries.SELECT_ALL_PERIODS);
            while (rs.next()) {
                list.add(extractPeriod(rs));
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_EXTRACT_PERIOD, ex);
        } finally {
            close(con, stmt, rs);
        }
        return list;
    }

    public Period findPeriod(long id){
        Period period = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.filterExactQuery(Queries.SELECT_ALL_PERIODS, Fields.PERIOD_ID));
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                period = extractPeriod(rs);
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_FIND_PERIOD, ex);
        } finally {
            close(con, stmt, rs);
        }
        return period;
    }

    /////////////////////////////////////////////
    //          Methods related to User        //
    ////////////////////////////////////////////

    public List<User> findUsers(){
        List<User> list = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(Queries.SELECT_ALL_USERS);
            while (rs.next()) {
                list.add(extractUser(rs));
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_EXTRACT_USER, ex);
        } finally {
            close(con, stmt, rs);
        }
        return list;
    }


    public User findUser(long id){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.filterExactQuery(Queries.SELECT_ALL_USERS, Fields.USER_ID));
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_FIND_USER, ex);
        } finally {
            close(con, stmt, rs);
        }
        return null;
    }

    private User findUserBy(String field, String value){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.filterExactQuery(Queries.SELECT_ALL_USERS, field));
            stmt.setString(1, value);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_FIND_USER, ex);
        } finally {
            close(con, stmt, rs);
        }
        return null;
    }

    private User findUserBy(String field, long numValue){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.filterExactQuery(Queries.SELECT_ALL_USERS, field));
            stmt.setLong(1, numValue);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_FIND_USER, ex);
        } finally {
            close(con, stmt, rs);
        }
        return null;
    }

    public User findUser(String login){
        return findUserBy(Fields.USER_LOGIN, login);
    }

    public User findUserByCookieToken(String tokenHash){
        return findUserBy(Fields.USER_TOKEN_HASH, tokenHash);
    }

    public User findUserByEmail(String email){
        return findUserBy(Fields.USER_EMAIL, email);
    }

    public boolean emailExists(String email){
        return findUserByEmail(email) != null;
    }

    public User findUserByConfirmationToken(String token){
        PreparedStatement stmt = null;
        Connection con = null;
        ResultSet rs = null;
        User user = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.FIND_USER_WITH_TOKEN);
            stmt.setString(1, token);
            rs= stmt.executeQuery();
            if (rs.next()) {
                user = extractUser(rs);
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_FIND_USER, ex);
        } finally {
            close(con, stmt);
        }
        return user;
    }

    public void updateTimestamp(User user){
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.UPDATE_TIMESTAMP);
            stmt.setLong(1, System.currentTimeMillis());
            stmt.setLong(2, user.getId());
            int rows = stmt.executeUpdate();
            if (rows != 1) {
                throw new SQLException(Messages.ERR_DATA_INTEGRITY);
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_UPDATE_USER, ex);
        } finally {
            close(con, stmt);
        }
    }

    public List<Periodical> findUserPeriodicals(long user_id){
        List<Periodical> periodicals = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.SELECT_USER_PERIODICALS);
            stmt.setLong(1, user_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                periodicals.add(extractPeriodical(rs));
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_FIND_USER_PERIODICALS, ex);
        } finally {
            close(con, stmt, rs);
        }
        return periodicals;
    }

    private void subscribe(User user, Periodical periodical) {
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.SUBSCRIBE_USER);
            log.debug("Subscribe query --> " + Queries.SUBSCRIBE_USER);
            stmt.setLong(1, user.getId());
            stmt.setLong(2, periodical.getId());
            int rowCount = stmt.executeUpdate();
            if (rowCount != 1){
                throw new SQLException("Row count not equal to 1 --> " + rowCount);
            }
            con.commit();
            updateUser(user);
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_SUBSRIBING_USER, ex);
        } finally {
            close(con, stmt);
        }
    }

    private void changeBalance(User user, String query){
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(query);
            stmt.setLong(1, user.getId());
            int rowCount = stmt.executeUpdate();
            if (rowCount != 1){
                throw new SQLException("Row count not equal to 1 --> " + rowCount);
            }
            con.commit();
            updateUser(user);
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_DECREASING_BALANCE, ex);
        } finally {
            close(con, stmt);
        }
    }

    public void decreaseBalance(User user, double amount){
        String query = Queries.decreaseUserBalance(amount);
        log.debug("Decrease query --> " + query);
        changeBalance(user, query);
    }

    public void increaseBalance(User user, double amount){
        String query = Queries.increaseUserBalance(amount);
        log.debug("Increase query --> " + query);
        changeBalance(user, query);
    }

    public void subscribeUser(User user, Periodical periodical) throws IllegalArgumentException {
        if (user.getBalance() >= periodical.getPrice()) {
            log.debug("User periodicals number before subscibtion --> " + user.getPeriodicals().size());
            decreaseBalance(user, periodical.getPrice());
            subscribe(user, periodical);
            log.debug("User periodicals number after subscibtion --> " + user.getPeriodicals().size());
        }
        else{
            throw new IllegalArgumentException("Too few money");
        }
    }

    public void insertUser(String login, String password, String email, long timestamp, String salt){
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.INSERT_USER);
            String passwordHash = Security.hexHash(password);
            stmt.setString(1, login);
            stmt.setString(2, passwordHash);
            stmt.setString(3, email);
            stmt.setLong(4, timestamp);
            stmt.setString(5, salt);
            int rowCount = stmt.executeUpdate();
            if (rowCount != 1){
                throw new SQLException("Row count not equal to 1 --> " + rowCount);
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_INSERT_USER, ex);
        } finally {
            close(con, stmt);
        }
    }

    private void setUserField(User user, String field, String value){
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.setUserField(field));
            stmt.setString(1, value);
            stmt.setLong(2, user.getId());
            int rowCount = stmt.executeUpdate();
            if (rowCount != 1){
                throw new SQLException("Row count not equal to 1 --> " + rowCount);
            }
            con.commit();
            updateUser(user);
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_UPDATE_USER, ex);
        } finally {
            close(con, stmt);
        }
    }

    public void setUserTokenHash(User user, String hash){
        setUserField(user, Fields.USER_TOKEN_HASH, hash);
    }

    public void setUserLocale(User user, String locale){
        setUserField(user, Fields.USER_LANG, locale);
    }

    public List<User> findClients(){
        List<User> users = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.filterUsers(Fields.ROLE_ID));
            stmt.setInt(1, Role.CLIENT.ordinal() + 1);
            rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(extractUser(rs));
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_FIND_CLIENTS, ex);
        } finally {
            close(con, stmt, rs);
        }
        return users;
    }

    public void toggleUserBlock(User user, boolean blockToBe){
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.toggleUserBlock(blockToBe));
            stmt.setLong(1, user.getId());
            int rowCount = stmt.executeUpdate();
            if (rowCount != 1){
                throw new SQLException("Row count not equal to 1 --> " + rowCount);
            }
            con.commit();
            updateUser(user);
            log.debug(String.format("Setting block to --> %s", user.isBlocked()));
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_TOGGLE_USER_BLOCK, ex);
        } finally {
            close(con, stmt);
        }
    }

    public void blockUser(User user){
        toggleUserBlock(user, true);
    }

    public void unblockUser(User user){
        toggleUserBlock(user, false);
    }

    public boolean userExists(String login){
        return findUser(login) != null;
    }

    @Override
    public User findUserByFBId(long FBId) {
        return findUserBy(Fields.FB_ID, FBId);
    }

    @Override
    public void insertFBUser(long FBId, String name) {
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.INSERT_FB_USER);
            stmt.setString(1, name);
            stmt.setLong(2, FBId);
            int rowCount = stmt.executeUpdate();
            if (rowCount != 1){
                throw new SQLException("Row count not equal to 1 --> " + rowCount);
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_INSERT_USER, ex);
        } finally {
            close(con, stmt);
        }
    }

    @Override
    public void updateUserLogin(long id, String newLogin) {
        PreparedStatement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.UPDATE_USER_LOGIN);
            stmt.setString(1, newLogin);
            stmt.setLong(2, id);
            int rowCount = stmt.executeUpdate();
            if (rowCount != 1){
                throw new SQLException("Row count not equal to 1 --> " + rowCount);
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_UPDATE_USER, ex);
        } finally {
            close(con, stmt);
        }
    }

    @Override
    public List<Sale> findSalesForGivenMonth(int month, int year) {
        List<Sale> list = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.prepareStatement(Queries.SALES_LIST_FOR_GIVEN_MONTH);
            stmt.setInt(1, month);
            stmt.setInt(2, year);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractSale(rs));
            }
            con.commit();
        } catch (SQLException ex) {
            rollback(con);
            log.error(ex.getMessage());
            throw new RuntimeException(Messages.ERR_CANNOT_FIND_SALES, ex);
        } finally {
            close(con, stmt, rs);
        }
        return list;
    }


    /////////////////////////////////////////////
    //               Updates                   //
    /////////////////////////////////////////////

    public void updateUser(User old){
        User newUser = findUser(old.getId());
        old.setId(newUser.getId());
        old.setLogin(newUser.getLogin());
        old.setPassword(newUser.getPassword());
        old.setBalance(newUser.getBalance());
        old.setPeriodicals(newUser.getPeriodicals());
        old.setBlocked(newUser.isBlocked());
        old.setLang(newUser.getLang());
        old.setRole(newUser.getRole());
        old.setTokenHash(newUser.getTokenHash());
        old.setEmail(newUser.getEmail());
    }

    ////////////////////////////////////////
    //             Extracts               //
    ////////////////////////////////////////

    public Period extractPeriod(ResultSet rs) throws SQLException {
        Period p = new Period();
        p.setId(rs.getLong(Fields.PERIOD_ID));
        p.setName(rs.getString(Fields.PERIOD_NAME));
        return p;
    }

    public Periodical extractPeriodical(ResultSet rs) throws SQLException {
        Periodical p = new Periodical();
        p.setId(rs.getLong(Fields.PERIODICAL_ID));
        p.setTitle(rs.getString(Fields.PERIODICAL_TITLE));
        p.setPrice(rs.getDouble(Fields.PERIODICAL_PRICE));
        p.setTheme(extractTheme(rs));
        log.debug(String.format("%s's theme --> %s", p.getTitle(), p.getTheme().toString()));
        p.setPeriod(extractPeriod(rs));
        return p;
    }

    public Role extractRole(ResultSet rs) throws SQLException {
        int id = rs.getInt(Fields.ROLE_ID);
        return Role.values()[id - 1];
    }

    public Theme extractTheme(ResultSet rs) throws SQLException {
        Theme t = new Theme();
        t.setId(rs.getLong(Fields.THEME_ID));
        t.setName(rs.getString(Fields.THEME_NAME));
        return t;
    }

    public User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong(Fields.USER_ID));
        user.setLogin(rs.getString(Fields.USER_LOGIN));
        user.setPassword(rs.getString(Fields.USER_PASSWORD));
        user.setBlocked(rs.getBoolean(Fields.USER_BLOCKED));
        user.setBalance(rs.getDouble(Fields.USER_BALANCE));
        user.setRole(extractRole(rs));
        user.setTokenHash(rs.getString(Fields.USER_TOKEN_HASH));
        user.setLang(rs.getString(Fields.USER_LANG));
        user.setPeriodicals(findUserPeriodicals(user.getId()));
        user.setEmail(rs.getString(Fields.USER_EMAIL));
        user.setFbId(rs.getLong(Fields.FB_ID));
        user.setHasOnlyFBAccount(rs.getBoolean(Fields.FB_ONLY));
        return user;
    }

    private Sale extractSale(ResultSet rs) throws SQLException{
        Sale sale = new Sale();
        sale.setPeriodical(extractPeriodical(rs));
        sale.setCount(rs.getInt(Fields.TOTAL_COUNT));
        sale.setSum(rs.getDouble(Fields.TOTAL_SUM));
        return sale;
    }
}

