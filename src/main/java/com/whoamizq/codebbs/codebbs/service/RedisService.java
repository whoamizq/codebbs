package com.whoamizq.codebbs.codebbs.service;

import com.whoamizq.codebbs.codebbs.entity.UserLike;

import java.util.List;

public interface RedisService {
    /**
     * 点赞
     * @param likedUserId 被点赞者
     * @param likedPostId 点赞者
     */
    void saveLikeRedis(String likedUserId, String likedPostId);
    /**
     * 被点赞数+1
     * @param likedUserId 被点赞者
     */
    void incrementLikedCount(String likedUserId);
    /**
     * 取消点赞
     * @param likedUserId 被点赞者
     * @param likedPostId 点赞者
     */
    void unlikeFromRedis(String likedUserId, String likedPostId);
    /**
     * 被点赞数11
     * @param likedUserId 被点赞者
     */
    void decrementLikedCount(String likedUserId);
    /**
     * 获取两者之间是否含有点赞关系
     * @param likedUserId 被点赞者
     * @param likedPostId 点赞者
     * @return
     */
    Integer getLikedStatus(String likedUserId, String likedPostId);
    /**
     * 获取Redis中存储的所有点赞数量
     * @return
     */
    List<UserLike> getLikedDataFromRedis();
    /**
     * 获取某个id的点赞总数
     * @param likedUserId 被点赞者
     * @return
     */
    Integer getLikedCount(String likedUserId);
}
