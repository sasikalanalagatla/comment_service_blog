package com.mb.commentService.service.impl;

import com.mb.commentService.dto.CommentDto;
import com.mb.commentService.exception.CommentNotFoundException;
import com.mb.commentService.exception.InvalidCommentDataException;
import com.mb.commentService.model.Comment;
import com.mb.commentService.repository.CommentRepository;
import com.mb.commentService.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        log.info("Creating comment for postId: {}", commentDto.getPostId());

        if (commentDto.getName() == null || commentDto.getName().trim().isEmpty()) {
            log.error("Invalid comment data: Name is missing");
            throw new InvalidCommentDataException("Name is required");
        }
        if (commentDto.getEmail() == null || commentDto.getEmail().trim().isEmpty()) {
            log.error("Invalid comment data: Email is missing");
            throw new InvalidCommentDataException("Email is required");
        }
        if (commentDto.getComment() == null || commentDto.getComment().trim().isEmpty()) {
            log.error("Invalid comment data: Comment text is missing");
            throw new InvalidCommentDataException("Comment is required");
        }
        if (commentDto.getPostId() == null) {
            log.error("Invalid comment data: PostId is missing");
            throw new InvalidCommentDataException("Post ID is required");
        }

        Comment comment = new Comment();
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setComment(commentDto.getComment());
        comment.setPostId(commentDto.getPostId());

        Comment savedComment = commentRepository.save(comment);
        log.debug("Saved comment with id: {}", savedComment.getId());

        return convertToDto(savedComment);
    }

    @Override
    public CommentDto getCommentById(Long id) {
        log.info("Fetching comment with id: {}", id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Comment not found with id: {}", id);
                    return new CommentNotFoundException("Comment not found with id: " + id);
                });
        return convertToDto(comment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        log.info("Fetching comments for postId: {}", postId);
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);

        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtos.add(convertToDto(comment));
        }

        log.debug("Fetched {} comments for postId: {}", commentDtos.size(), postId);
        return commentDtos;
    }

    @Override
    public CommentDto updateComment(Long id, CommentDto commentDto) {
        log.info("Updating comment with id: {}", id);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cannot update, comment not found with id: {}", id);
                    return new CommentNotFoundException("Comment not found with id: " + id);
                });

        if (commentDto.getName() == null || commentDto.getName().trim().isEmpty()) {
            log.error("Invalid update data: Name is missing");
            throw new InvalidCommentDataException("Name is required");
        }
        if (commentDto.getEmail() == null || commentDto.getEmail().trim().isEmpty()) {
            log.error("Invalid update data: Email is missing");
            throw new InvalidCommentDataException("Email is required");
        }
        if (commentDto.getComment() == null || commentDto.getComment().trim().isEmpty()) {
            log.error("Invalid update data: Comment text is missing");
            throw new InvalidCommentDataException("Comment is required");
        }

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setComment(commentDto.getComment());

        Comment updatedComment = commentRepository.save(comment);
        log.debug("Updated comment with id: {}", updatedComment.getId());

        return convertToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long id) {
        log.info("Deleting comment with id: {}", id);

        if (!commentRepository.existsById(id)) {
            log.warn("Cannot delete, comment not found with id: {}", id);
            throw new CommentNotFoundException("Comment not found with id: " + id);
        }

        commentRepository.deleteById(id);
        log.debug("Deleted comment with id: {}", id);
    }

    @Override
    public void deleteCommentsByPostId(Long postId) {
        log.info("Deleting all comments for postId: {}", postId);
        commentRepository.deleteByPostId(postId);
        log.debug("Deleted all comments for postId: {}", postId);
    }

    private CommentDto convertToDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getName(),
                comment.getEmail(),
                comment.getComment(),
                comment.getPostId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}