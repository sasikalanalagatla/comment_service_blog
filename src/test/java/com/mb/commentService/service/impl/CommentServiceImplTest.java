package com.mb.commentService.service.impl;

import com.mb.commentService.dto.CommentDto;
import com.mb.commentService.exception.CommentNotFoundException;
import com.mb.commentService.exception.InvalidCommentDataException;
import com.mb.commentService.model.Comment;
import com.mb.commentService.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    @Spy
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createComment_success() {
        CommentDto dto = new CommentDto();
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setComment("Hello!");
        dto.setPostId(10L);

        Comment savedEntity = new Comment();
        savedEntity.setId(1L);
        savedEntity.setName(dto.getName());
        savedEntity.setEmail(dto.getEmail());
        savedEntity.setComment(dto.getComment());
        savedEntity.setPostId(dto.getPostId());

        when(commentRepository.save(any(Comment.class))).thenReturn(savedEntity);

        CommentDto result = commentService.createComment(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Hello!", result.getComment());

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void createComment_missingName_throwsException() {
        CommentDto dto = new CommentDto();
        dto.setEmail("john@example.com");
        dto.setComment("Hello!");
        dto.setPostId(10L);

        InvalidCommentDataException ex = assertThrows(
                InvalidCommentDataException.class,
                () -> commentService.createComment(dto)
        );

        assertEquals("Name is required", ex.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void createComment_missingEmail_throwsException() {
        CommentDto dto = new CommentDto();
        dto.setName("John");
        dto.setComment("Hello!");
        dto.setPostId(10L);

        InvalidCommentDataException ex = assertThrows(
                InvalidCommentDataException.class,
                () -> commentService.createComment(dto)
        );

        assertEquals("Email is required", ex.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void createComment_missingComment_throwsException() {
        CommentDto dto = new CommentDto();
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setPostId(10L);

        InvalidCommentDataException ex = assertThrows(
                InvalidCommentDataException.class,
                () -> commentService.createComment(dto)
        );

        assertEquals("Comment is required", ex.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void createComment_missingPostId_throwsException() {
        CommentDto dto = new CommentDto();
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setComment("Hello!");

        InvalidCommentDataException ex = assertThrows(
                InvalidCommentDataException.class,
                () -> commentService.createComment(dto)
        );

        assertEquals("Post ID is required", ex.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void getCommentById() {
        Long commentId = 1L;
        CommentDto dto = new CommentDto();
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setComment("Hello!");
        dto.setPostId(10L);

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setName(dto.getName());
        comment.setEmail(dto.getEmail());
        comment.setComment(dto.getComment());
        comment.setPostId(dto.getPostId());

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        CommentDto result = commentService.getCommentById(commentId);
        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(commentService, times(1)).getCommentById(commentId);
    }

    @Test
    void getCommentById_commentNotFound() {
        Long commentId = 1L;
        CommentDto dto = new CommentDto();
        dto.setEmail("john@example.com");
        dto.setComment("Hello!");
        dto.setId(10L);
        dto.setPostId(10L);

        CommentNotFoundException ex = assertThrows(
                CommentNotFoundException.class, ()-> commentService.getCommentById(commentId)
        );

        assertEquals("Comment not found with id: "+commentId, ex.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void getCommentsByPostId() {
        Long postId = 1L;

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setEmail("email@gmail.com");
        comment.setId(1L);
        comment.setName("name");
        comment.setComment("hi");
        List<Comment> mockComments = List.of(comment);

        when(commentRepository.findByPostIdOrderByCreatedAtDesc(postId)).thenReturn(mockComments);
        List<CommentDto> result = commentService.getCommentsByPostId(postId);
        assertNotNull(result);

        verify(commentService, times(1)).getCommentsByPostId(postId);
    }

    @Test
    void getCommentsByPostId_commentNotFound() {
        Long postId = 1L;

        Comment comment = new Comment();
        comment.setPostId(2L);
        comment.setEmail("email@gmail.com");
        comment.setId(1L);
        comment.setName("name");
        comment.setComment("hi");

        CommentNotFoundException ex = assertThrows(
                CommentNotFoundException.class, ()-> commentService.getCommentsByPostId(postId)
        );

        assertEquals("No comments found for postId: "+postId, ex.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void updateComment() {
        Long commentId = 1L;
        CommentDto dto = new CommentDto();
        dto.setEmail("john@example.com");
        dto.setName("name");
        dto.setComment("Hello!");
        dto.setId(commentId);
        dto.setPostId(2L);

        Comment comment = new Comment();
        comment.setPostId(2L);
        comment.setEmail("email@gmail.com");
        comment.setId(commentId);
        comment.setName("name");
        comment.setComment("hi");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);
        CommentDto result = commentService.updateComment(commentId,dto);
        assertNotNull(result);

        verify(commentService, times(1)).updateComment(commentId, dto);

    }

    @Test
    void deleteComment() {
        Long commentId = 1L;
        Comment comment = new Comment();
        comment.setPostId(2L);
        comment.setEmail("email@gmail.com");
        comment.setId(commentId);
        comment.setName("name");
        comment.setComment("hi");

        when(commentRepository.existsById(commentId)).thenReturn(true);
        commentRepository.deleteById(commentId);

        verify(commentRepository, times(1)).deleteById(commentId);

    }

    @Test
    void deleteCommentsByPostId() {
        Long postId = 1L;

        commentService.deleteCommentsByPostId(postId);
        verify(commentService, times(1)).deleteCommentsByPostId(postId);
    }
}