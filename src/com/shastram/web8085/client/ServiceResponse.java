package com.shastram.web8085.client;

import java.io.Serializable;

public class ServiceResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean loginRequired = false;
    private String msg;

    public ServiceResponse() {
    }

    public ServiceResponse(boolean loginRequired) {
        this.loginRequired = loginRequired;
    }

    public ServiceResponse(String msg) {
        this.msg = msg;
    }

    public boolean isLoginRequired() {
        return loginRequired;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
