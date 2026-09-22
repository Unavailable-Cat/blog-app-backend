package com.article.Exceptions;

public class NullPasswordException extends RuntimeException {
    public NullPasswordException(String message) {
        super(message);
    }
}
