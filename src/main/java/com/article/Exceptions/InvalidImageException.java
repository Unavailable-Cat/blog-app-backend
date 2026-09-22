package com.article.Exceptions;

public class InvalidImageException extends RuntimeException{
    public InvalidImageException(String message) {
        super(message);
    }
}
