package com.mb.commentService.controller;

import com.mb.commentService.dto.CommentDto;
import com.mb.commentService.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentControllerTest {

    @Mock
    private CommentServiceImpl commentService;

    @InjectMocks
    @Spy
    private CommentController commentController;

    public CommentControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createComment() {
        CommentDto inputDto = new CommentDto();
        inputDto.setName("John");
        inputDto.setEmail("john@example.com");
        inputDto.setComment("Hello!");
        inputDto.setPostId(100L);

        CommentDto savedDto = new CommentDto();
        savedDto.setId(1L);
        savedDto.setName("John");
        savedDto.setEmail("john@example.com");
        savedDto.setComment("Hello!");
        savedDto.setPostId(100L);

        when(commentService.createComment(inputDto)).thenReturn(savedDto);

        ResponseEntity<CommentDto> response = commentController.createComment(inputDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());

        verify(commentService, times(1)).createComment(inputDto);
    }

    @Test
    void getCommentById() {
        Long commentId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentId);
        commentDto.setComment("hi");

        when(commentService.getCommentById(commentId)).thenReturn(commentDto);

        ResponseEntity<CommentDto> response = commentController.getCommentById(commentId);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());

        verify(commentService, times(1)).getCommentById(commentId);
    }

    @Test
    void getCommentsByPostId() {
        Long postId = 1L;
        CommentDto comment1 = new CommentDto();
        comment1.setComment("Hi");
        comment1.setPostId(postId);
        comment1.setId(1L);
        CommentDto comment2 = new CommentDto();
        comment1.setComment("Hi");
        comment1.setPostId(postId);
        comment1.setId(2L);
        CommentDto comment3 = new CommentDto();
        comment1.setComment("Hi");
        comment1.setPostId(postId);
        comment1.setId(3L);

        List<CommentDto> commentDtos = Arrays.asList(comment1,comment2,comment3);

        when(commentService.getCommentsByPostId(postId)).thenReturn(commentDtos);

        ResponseEntity<List<CommentDto>> response = commentController.getCommentsByPostId(postId);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());

        verify(commentService, times(1)).getCommentsByPostId(postId);
    }

    @Test
    void updateComment() {
        Long commentId = 1L;
        CommentDto comment = new CommentDto();
        comment.setComment("Hi");
        comment.setPostId(1L);
        comment.setId(commentId);

        when(commentService.updateComment(commentId,comment)).thenReturn(comment);

        ResponseEntity<CommentDto> response = commentController.updateComment(commentId,comment);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());

        verify(commentService, times(1)).updateComment(commentId,comment);
    }

    @Test
    void deleteComment() {
        Long commentId = 1L;

        commentService.deleteComment(commentId);

        ResponseEntity<Void> response = commentController.deleteComment(commentId);
        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());

        verify(commentService, times(2)).deleteComment(commentId);
    }

    @Test
    void deleteCommentsByPostId() {
        Long postId = 1L;
        CommentDto comment = new CommentDto();
        comment.setComment("Hi");
        comment.setPostId(postId);
        comment.setId(1L);

        commentService.deleteCommentsByPostId(postId);

        ResponseEntity<Void> response = commentController.deleteCommentsByPostId(postId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(commentService, times(2)).deleteCommentsByPostId(postId);
    }
}