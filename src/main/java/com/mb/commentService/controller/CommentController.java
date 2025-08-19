package com.mb.commentService.controller;

import com.mb.commentService.dto.CommentDto;
import com.mb.commentService.service.impl.CommentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Slf4j
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto) {
        log.info("Received request to create comment: {}", commentDto);
        CommentDto createdComment = commentService.createComment(commentDto);
        log.debug("Created comment: {}", createdComment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        log.info("Fetching comment with id: {}", id);
        CommentDto comment = commentService.getCommentById(id);
        log.debug("Fetched comment: {}", comment);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable Long postId) {
        log.info("Fetching comments for postId: {}", postId);
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        log.debug("Fetched {} comments for postId: {}", comments.size(), postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        log.info("Updating comment with id: {}", id);
        CommentDto updatedComment = commentService.updateComment(id, commentDto);
        log.debug("Updated comment: {}", updatedComment);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        log.warn("Deleting comment with id: {}", id);
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> deleteCommentsByPostId(@PathVariable Long postId) {
        log.warn("Deleting all comments for postId: {}", postId);
        commentService.deleteCommentsByPostId(postId);
        return ResponseEntity.noContent().build();
    }
}