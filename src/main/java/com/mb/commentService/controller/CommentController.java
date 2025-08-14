package com.mb.commentService.controller;

import com.mb.commentService.dto.CommentDto;
import com.mb.commentService.service.impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
@Validated
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentDto commentDto) {
        try {
            if (commentDto.getName() == null || commentDto.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Name is required");
            }
            if (commentDto.getEmail() == null || commentDto.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (commentDto.getComment() == null || commentDto.getComment().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Comment is required");
            }
            if (commentDto.getPostId() == null) {
                return ResponseEntity.badRequest().body("Post ID is required");
            }

            CommentDto createdComment = commentService.createComment(commentDto);
            return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create comment: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        try {
            CommentDto comment = commentService.getCommentById(id);
            return ResponseEntity.ok(comment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve comment: " + e.getMessage());
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId) {
        try {
            List<CommentDto> comments = commentService.getCommentsByPostId(postId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve comments: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        try {
            if (commentDto.getName() == null || commentDto.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Name is required");
            }
            if (commentDto.getEmail() == null || commentDto.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (commentDto.getComment() == null || commentDto.getComment().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Comment is required");
            }

            CommentDto updatedComment = commentService.updateComment(id, commentDto);
            return ResponseEntity.ok(updatedComment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update comment: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete comment: " + e.getMessage());
        }
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> deleteCommentsByPostId(@PathVariable Long postId) {
        try {
            commentService.deleteCommentsByPostId(postId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete comments: " + e.getMessage());
        }
    }
}