package com.example.iot.exception;

public class ParamaErrorException extends RuntimeException {
    public ParamaErrorException() {
    }

    public ParamaErrorException(String message) {
        super(message);
    }

}
