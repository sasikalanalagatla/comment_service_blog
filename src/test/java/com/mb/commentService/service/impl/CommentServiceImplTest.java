package com.mb.commentService.service.impl;

import com.mb.commentService.dto.CommentDto;
import com.mb.commentService.exception.CommentNotFoundException;
import com.mb.commentService.exception.InvalidCommentDataException;
import com.mb.commentService.model.Comment;
import com.mb.commentService.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private CommentDto validDto;
    private Comment validComment;

    @BeforeEach
    void setUp() {
        validDto = new CommentDto();
        validDto.setId(1L);
        validDto.setName("John");
        validDto.setEmail("john@example.com");
        validDto.setComment("Hello!");
        validDto.setPostId(10L);

        validComment = new Comment();
        validComment.setId(1L);
        validComment.setName("John");
        validComment.setEmail("john@example.com");
        validComment.setComment("Hello!");
        validComment.setPostId(10L);
    }

    @Test
    void createComment_success() {
        when(commentRepository.save(any(Comment.class))).thenReturn(validComment);

        CommentDto result = commentService.createComment(validDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Hello!", result.getComment());

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void createComment_missingName_throwsException() {
        validDto.setName(null);

        InvalidCommentDataException ex = assertThrows(
                InvalidCommentDataException.class,
                () -> commentService.createComment(validDto)
        );

        assertEquals("Name is required", ex.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void createComment_missingEmail_throwsException() {
        validDto.setEmail(null);

        InvalidCommentDataException ex = assertThrows(
                InvalidCommentDataException.class,
                () -> commentService.createComment(validDto)
        );

        assertEquals("Email is required", ex.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void createComment_missingComment_throwsException() {
        validDto.setComment(null);

        InvalidCommentDataException ex = assertThrows(
                InvalidCommentDataException.class,
                () -> commentService.createComment(validDto)
        );

        assertEquals("Comment is required", ex.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void createComment_missingPostId_throwsException() {
        validDto.setPostId(null);

        InvalidCommentDataException ex = assertThrows(
                InvalidCommentDataException.class,
                () -> commentService.createComment(validDto)
        );

        assertEquals("Post ID is required", ex.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void getCommentById_success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(validComment));

        CommentDto result = commentService.getCommentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(commentRepository, times(1)).findById(1L);
    }

    @Test
    void getCommentById_commentNotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        CommentNotFoundException ex = assertThrows(
                CommentNotFoundException.class,
                () -> commentService.getCommentById(1L)
        );

        assertEquals("Comment not found with id: 1", ex.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void getCommentsByPostId_success() {
        when(commentRepository.findByPostIdOrderByCreatedAtDesc(10L))
                .thenReturn(List.of(validComment));

        List<CommentDto> result = commentService.getCommentsByPostId(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getPostId());

        verify(commentRepository, times(1)).findByPostIdOrderByCreatedAtDesc(10L);
    }

    @Test
    void getCommentsByPostId_noComments_returnsEmptyList() {
        when(commentRepository.findByPostIdOrderByCreatedAtDesc(99L))
                .thenReturn(List.of());

        List<CommentDto> comments = commentService.getCommentsByPostId(99L);

        assertNotNull(comments);
        assertTrue(comments.isEmpty());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void updateComment_success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(validComment));
        when(commentRepository.save(validComment)).thenReturn(validComment);

        CommentDto result = commentService.updateComment(1L, validDto);

        assertNotNull(result);
        assertEquals("Hello!", result.getComment());

        verify(commentRepository, times(1)).findById(1L);
    }

    @Test
    void deleteComment_success() {
        when(commentRepository.existsById(1L)).thenReturn(true);

        commentService.deleteComment(1L);

        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCommentsByPostId_success() {
        commentService.deleteCommentsByPostId(10L);

        verify(commentRepository, times(1)).deleteByPostId(10L);
    }
}