package com.evs.doctor.service;

public class AuthorizationException extends ApplicationException {

    public AuthorizationException(String detailMessage) {
        super(detailMessage);

    }

    public AuthorizationException() {
        super("");

    }

}
