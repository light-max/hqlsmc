package com.hql.scm.fieldcheck;

public class FieldCheckException extends RuntimeException {
    private final String message;

    public FieldCheckException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
