package com.mb.commentService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String name;
    private String email;
    private String comment;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
