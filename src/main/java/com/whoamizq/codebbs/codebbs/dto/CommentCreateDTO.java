package com.whoamizq.codebbs.codebbs.dto;

import lombok.Data;

/**
 * 用于展示评论所提供的参数
 * parentId： 评论的问题或者评论的id
 * type：1表示评论的是问题，2表示评论的是评论
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
    private Integer commentCount;
}
