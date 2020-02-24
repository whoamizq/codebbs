package com.whoamizq.codebbs.codebbs.utils;

/**
 * 根据规则生成对应的key
 */
public class RedisKeyUtil {
    //保存用户点赞数据的key
    public static final String MAP_KEY_USER_LIKED = "MAP_USER_LIKED";
    //保存用户被点赞数量的key
    public static final String MAP_KEY_USER_LIKED_COUNT = "MAP_USER_LIKED_COUNT";

    /**
     * 拼接被点赞的用户id和点赞的人的id作为key。xxx::xxx
     * @param likedUserId 被点赞的人的id
     * @param likedPostId 点赞的人的id
     * @return
     */
    public static String getLikedKey(String likedUserId,String likedPostId){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(likedUserId);
        stringBuilder.append("::");
        stringBuilder.append(likedPostId);
        return stringBuilder.toString();
    }
}
