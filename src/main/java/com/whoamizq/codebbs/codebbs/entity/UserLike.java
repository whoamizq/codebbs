package com.whoamizq.codebbs.codebbs.entity;

import com.whoamizq.codebbs.codebbs.enums.LikedStatusEnum;
import lombok.Data;

/**
 * 用户点赞表
 */
@Data
public class UserLike {
    private Integer id;
    //被点赞的用户id
    private String likedUserId;
    //点赞的用户id
    private String likedPostId;
    //点赞的状态，默认未点赞
    private Integer status = LikedStatusEnum.UNLIKE.getCode();

    public UserLike(){ }

    public UserLike(String likedUserId,String likedPostId,Integer status){
        this.likedPostId = likedPostId;
        this.likedUserId = likedUserId;
        this.status = status;
    }
}
