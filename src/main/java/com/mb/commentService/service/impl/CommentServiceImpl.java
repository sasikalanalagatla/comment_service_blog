package com.mb.commentService.service.impl;

import com.mb.commentService.dto.CommentDto;
import com.mb.commentService.exception.CommentNotFoundException;
import com.mb.commentService.model.Comment;
import com.mb.commentService.repository.CommentRepository;
import com.mb.commentService.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setComment(commentDto.getComment());
        comment.setPostId(commentDto.getPostId());

        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }

    @Override
    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        return convertToDto(comment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtos.add(convertToDto(comment));
        }
        return commentDtos;
    }

    @Override
    public CommentDto updateComment(Long id, CommentDto commentDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setComment(commentDto.getComment());

        Comment updatedComment = commentRepository.save(comment);
        return convertToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }

    @Override
    public void deleteCommentsByPostId(Long postId) {
        commentRepository.deleteByPostId(postId);
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