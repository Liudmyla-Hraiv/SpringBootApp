package com.liutyk.first_demo.services;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(Long id) {
        super("Session with ID = " + id + " is not found");
    }
}
