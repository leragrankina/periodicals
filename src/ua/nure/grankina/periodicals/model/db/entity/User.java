package ua.nure.grankina.periodicals.model.db.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * User entity
 *
 * Created by Valeriia on 03.01.2017.
 */
public class User extends Entity{
    private String login;
    private String password;
    private List<Periodical> periodicals = new ArrayList<>();
    private Double balance = 0.0;
    private Role role = Role.CLIENT;
    private boolean blocked;
    private String lang = "en";
    private String tokenHash = "";
    private String email;
    private long timestamp;
    private String salt;
    private long fbId;
    private boolean hasOnlyFBAccount;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Periodical> getPeriodicals(){
        return periodicals;
    }

    public void setPeriodicals(List<Periodical> periodicalList){
        periodicals = periodicalList;
    }

    public boolean isSubscribed(Periodical p){
        return periodicals.contains(p);
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public long getFbId() {
        return fbId;
    }

    public void setFbId(long fbId) {
        this.fbId = fbId;
    }

    public boolean hasOnlyFBAccount() {
        return hasOnlyFBAccount;
    }

    public void setHasOnlyFBAccount(boolean hasOnlyFBAccount) {
        this.hasOnlyFBAccount = hasOnlyFBAccount;
    }

    @Override
    public String toString() {
        return String.format("User: %d %s", getId(), login);
    }
}
