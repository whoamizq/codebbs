package com.whoamizq.codebbs.codebbs.dto;

import lombok.Data;

/**
 * 被点赞次数
 */
@Data
public class LikedCountDTO {
    private String likedUserId;
    private Integer count;

    public LikedCountDTO(){}

    public LikedCountDTO(String likedUserId,Integer count){
        this.count = count;
        this.likedUserId = likedUserId;
    }
}
