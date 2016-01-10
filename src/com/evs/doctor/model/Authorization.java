package com.evs.doctor.model;

public class Authorization {
    private String userName;
    private String passwordHash;
    private String token;

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the passwordHash
     */
    public String getPassword() {
        return passwordHash;
    }

    /**
     * @param passwordHash the passwordHash to set
     */
    public void setPassword(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public final void setToken(String token) {
        this.token = token;
    }

}
