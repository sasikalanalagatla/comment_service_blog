package com.mb.commentService.exception;

public class CommentNotFoundException extends RuntimeException {
    
    public CommentNotFoundException(String message) {
        super(message);
    }
    
    public CommentNotFoundException(Long id) {
        super("Comment not found with id: " + id);
    }
}
