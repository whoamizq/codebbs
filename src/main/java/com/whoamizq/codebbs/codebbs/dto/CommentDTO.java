package com.whoamizq.codebbs.codebbs.dto;

import com.whoamizq.codebbs.codebbs.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long parentId;
    private Long id;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Integer commentCount;
    private String content;
    private User user;
}
