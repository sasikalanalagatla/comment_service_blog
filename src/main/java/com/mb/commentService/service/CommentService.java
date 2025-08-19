package com.mb.commentService.service;


import com.mb.commentService.dto.CommentDto;

import java.util.List;

public interface CommentService {
    
    CommentDto createComment(CommentDto commentDto);
    
    CommentDto getCommentById(Long id);
    
    List<CommentDto> getCommentsByPostId(Long postId);

    void deleteCommentsByPostId(Long postId);
    
    CommentDto updateComment(Long id, CommentDto commentDto);
    
    void deleteComment(Long id);
}
