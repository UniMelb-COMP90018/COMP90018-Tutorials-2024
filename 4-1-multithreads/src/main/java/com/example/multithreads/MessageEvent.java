package com.example.multithreads;

// Message class for implementing EventBus library
public class MessageEvent {

    public final String message;

    public MessageEvent(String message) {
        this.message = message;
    }
}
