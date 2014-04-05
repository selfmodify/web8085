package com.shastram.web8085.client;

import java.io.Serializable;

public class ServiceResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean loginRequired = false;

    public ServiceResponse() {
    }

    public ServiceResponse(boolean loginRequired) {
        this.loginRequired = loginRequired;
    }

    public boolean isLoginRequired() {
        return loginRequired;
    }
}
