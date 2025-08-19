package com.mb.commentService.exception;


public class InvalidCommentDataException extends RuntimeException {
    public InvalidCommentDataException(String message) {
        super(message);
    }
}
