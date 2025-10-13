package com.DatLeo.BookShop.exception;

import org.springframework.validation.BindingResult;

public class FieldException extends RuntimeException {

    private BindingResult bindingResult;

    public FieldException(BindingResult bindingResult) {
        super("Validation failed");
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
