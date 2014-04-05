package com.shastram.web8085.client;

import java.io.Serializable;

public class LoginData implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean prodMode;
    private String loginUrl;
    private String logoutUrl;

    public LoginData() {
    }

    public LoginData(boolean prodMode) {
        this.prodMode = prodMode;
    }

    public boolean isProdMode() {
        return prodMode;
    }

    public void setProdMode(boolean prodMode) {
        this.prodMode = prodMode;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }
}
