package com.mb.commentService.controller;

import com.mb.commentService.dto.CommentDto;
import com.mb.commentService.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentServiceImpl commentService;

    @InjectMocks
    private CommentController commentController;

    private CommentDto validDto;
    private CommentDto savedDto;
    private List<CommentDto> commentList;

    @BeforeEach
    void setUp() {
        validDto = new CommentDto();
        validDto.setId(null);
        validDto.setName("John");
        validDto.setEmail("john@example.com");
        validDto.setComment("Hello!");
        validDto.setPostId(100L);

        savedDto = new CommentDto();
        savedDto.setId(1L);
        savedDto.setName("John");
        savedDto.setEmail("john@example.com");
        savedDto.setComment("Hello!");
        savedDto.setPostId(100L);

        CommentDto c1 = new CommentDto();
        c1.setId(1L);
        c1.setComment("Hi");
        c1.setPostId(100L);

        CommentDto c2 = new CommentDto();
        c2.setId(2L);
        c2.setComment("Hello");
        c2.setPostId(100L);

        CommentDto c3 = new CommentDto();
        c3.setId(3L);
        c3.setComment("Hey");
        c3.setPostId(100L);

        commentList = Arrays.asList(c1, c2, c3);
    }

    @Test
    void createComment() {
        when(commentService.createComment(validDto)).thenReturn(savedDto);

        ResponseEntity<CommentDto> response = commentController.createComment(validDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());

        verify(commentService, times(1)).createComment(validDto);
    }

    @Test
    void getCommentById() {
        when(commentService.getCommentById(1L)).thenReturn(savedDto);

        ResponseEntity<CommentDto> response = commentController.getCommentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());

        verify(commentService, times(1)).getCommentById(1L);
    }

    @Test
    void getCommentsByPostId() {
        when(commentService.getCommentsByPostId(100L)).thenReturn(commentList);

        ResponseEntity<List<CommentDto>> response = commentController.getCommentsByPostId(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());

        verify(commentService, times(1)).getCommentsByPostId(100L);
    }

    @Test
    void updateComment() {
        when(commentService.updateComment(1L, savedDto)).thenReturn(savedDto);

        ResponseEntity<CommentDto> response = commentController.updateComment(1L, savedDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Hello!", response.getBody().getComment());

        verify(commentService, times(1)).updateComment(1L, savedDto);
    }

    @Test
    void deleteComment() {
        ResponseEntity<Void> response = commentController.deleteComment(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(commentService, times(1)).deleteComment(1L);
    }

    @Test
    void deleteCommentsByPostId() {
        ResponseEntity<Void> response = commentController.deleteCommentsByPostId(100L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(commentService, times(1)).deleteCommentsByPostId(100L);
    }
}