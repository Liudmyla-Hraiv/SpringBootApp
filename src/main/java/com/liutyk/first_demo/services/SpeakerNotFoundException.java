package com.liutyk.first_demo.services;

public class SpeakerNotFoundException extends RuntimeException {
    public SpeakerNotFoundException(Long id) {
        super("Speaker with ID = " + id + " is not found");
    }
}
