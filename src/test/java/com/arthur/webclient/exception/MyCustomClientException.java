package com.arthur.webclient.exception;

public class MyCustomClientException extends Exception {
    public MyCustomClientException(String errorMessage) {
        super(errorMessage);
    }
}
