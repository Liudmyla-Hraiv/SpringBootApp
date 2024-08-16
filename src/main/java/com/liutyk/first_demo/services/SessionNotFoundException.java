package com.liutyk.first_demo.services;

public class SessionNotFoundException extends Throwable {
    public SessionNotFoundException(Long id) {
        super("Session with ID = " + id + " is not found");
    }
}
