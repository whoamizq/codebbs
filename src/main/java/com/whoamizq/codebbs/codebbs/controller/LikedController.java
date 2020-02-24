package com.whoamizq.codebbs.codebbs.controller;

import com.whoamizq.codebbs.codebbs.entity.JsonData;
import com.whoamizq.codebbs.codebbs.model.User;
import com.whoamizq.codebbs.codebbs.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LikedController {
    @Autowired
    private RedisService redisService;

    /**
     * 进行点赞
     * @param likedUserId
     * @param session
     * @return
     */
    @GetMapping("/like/{likedUserId}")
    @ResponseBody
    public JsonData saveLikedRedis(@PathVariable(value = "likedUserId")String likedUserId,
                                   HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null){
            return JsonData.buildError("您还未登录，点赞失败",200);
        }
        redisService.saveLikeRedis(likedUserId,String.valueOf(user.getId()));
        //被点赞数+1
        redisService.incrementLikedCount(likedUserId);
        return JsonData.buildSuccess("点赞成功");
    }

    /**
     * 取消点赞
     * @param likedUserId
     * @param session
     * @return
     */
    @GetMapping("/unlike/{likedUserId}")
    @ResponseBody
    public JsonData saveUnLikedRedis(@PathVariable(value = "likedUserId")String likedUserId,
                                   HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null){
            return JsonData.buildError("您还未登录，取消失败",200);
        }
        redisService.unlikeFromRedis(likedUserId,String.valueOf(user.getId()));
        //被点赞数-1
        redisService.decrementLikedCount(likedUserId);
        return JsonData.buildSuccess("取消成功");
    }

    /**
     * 查询用户是否点赞过此评论Id
     * @param likedUserId
     * @param session
     * @return
     */
    @GetMapping("/getLikedStatus/{likedUserId}")
    @ResponseBody
    public Object getLikedStatus(@PathVariable(value = "likedUserId") String likedUserId,
                                 HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null){
            return JsonData.buildError("您还未登录",200);
        }
        Integer likedStatus = redisService.getLikedStatus(likedUserId, String.valueOf(user.getId()));
        if (likedStatus == null || likedStatus.equals(0)){
            return JsonData.buildSuccess("您还没点过赞", 404);
        }
        return JsonData.buildSuccess("已经点过赞了", 666);
    }
    @GetMapping("/getLikedData")
    @ResponseBody
    public Object getLikedData(){
        return redisService.getLikedDataFromRedis();
    }
}
