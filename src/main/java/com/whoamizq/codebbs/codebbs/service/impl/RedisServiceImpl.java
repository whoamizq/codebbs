package com.whoamizq.codebbs.codebbs.service.impl;

import com.whoamizq.codebbs.codebbs.dto.LikedCountDTO;
import com.whoamizq.codebbs.codebbs.entity.UserLike;
import com.whoamizq.codebbs.codebbs.enums.LikedStatusEnum;
import com.whoamizq.codebbs.codebbs.service.RedisService;
import com.whoamizq.codebbs.codebbs.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Redis实现点赞
 */
@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
    @Override
    public void saveLikeRedis(String likedUserId, String likedPostId) {
        String key = RedisKeyUtil.getLikedKey(likedUserId,likedPostId);
        redisTemplate.opsForHash().put(RedisKeyUtil.MAP_KEY_USER_LIKED, key, LikedStatusEnum.LIKE.getCode());
    }
    //被点赞数+1
    @Override
    public void incrementLikedCount(String likedUserId) {
        redisTemplate.opsForHash().increment(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT,likedUserId,1);
    }
    //取消点赞
    @Override
    public void unlikeFromRedis(String likedUserId, String likedPostId) {
        String key = RedisKeyUtil.getLikedKey(likedUserId, likedPostId);
        redisTemplate.opsForHash().put(RedisKeyUtil.MAP_KEY_USER_LIKED, key, LikedStatusEnum.UNLIKE.getCode());
    }
    //被点赞数-1
    @Override
    public void decrementLikedCount(String likedUserId) {
        redisTemplate.opsForHash().increment(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, likedUserId,-1);
    }
    //获取两者之间的点赞关系
    @Override
    public Integer getLikedStatus(String likedUserId, String likedPostId) {
        String key = RedisKeyUtil.getLikedKey(likedUserId, likedPostId);
        return (Integer) redisTemplate.opsForHash().get(RedisKeyUtil.MAP_KEY_USER_LIKED, key);
    }
    //获取所有点赞数
    @Override
    public List<UserLike> getLikedDataFromRedis() {
        Cursor<Map.Entry<Object,Object>> cursor = redisTemplate.opsForHash()
                .scan(RedisKeyUtil.MAP_KEY_USER_LIKED, ScanOptions.NONE);
        List<UserLike> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object,Object> entry = cursor.next();
            String key = (String)entry.getKey();
            //分离出likedUserId，以及likedPostId
            String[] split = key.split("::");
            String likedUserId = split[0];
            String likedPostId = split[1];
            Integer value = (Integer) entry.getValue();
            //组装成UserLike对象
            UserLike userLike = new UserLike(likedUserId,likedPostId,value);
            list.add(userLike);
        }
        return list;
    }
    //获取某个id的点赞总数
    @Override
    public Integer getLikedCount(String likedUserId) {
        return (Integer) redisTemplate.opsForHash().get(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, likedUserId);
    }
    //获取Redis中存储的所有点赞数量
    @Override
    public List<LikedCountDTO> getLikedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash()
                .scan(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
        List<LikedCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 LikedCountDT
            String key = (String)map.getKey();
            LikedCountDTO dto = new LikedCountDTO(key, (Integer) map.getValue());
            list.add(dto);
            //从Redis中删除这条记录
            //redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, key);
        }
        return list;
    }
    //删除数据
    @Override
    public void deleteLikedFromRedis(String likedUserId, String likedPostId) {
        String key = RedisKeyUtil.getLikedKey(likedUserId, likedPostId);
        redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_LIKED, key);
    }
}
