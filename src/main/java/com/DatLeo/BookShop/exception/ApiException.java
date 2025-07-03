package com.DatLeo.BookShop.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
