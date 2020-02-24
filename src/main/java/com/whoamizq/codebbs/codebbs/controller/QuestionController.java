package com.whoamizq.codebbs.codebbs.controller;

import com.whoamizq.codebbs.codebbs.dto.CommentDTO;
import com.whoamizq.codebbs.codebbs.dto.QuestionDTO;
import com.whoamizq.codebbs.codebbs.enums.CommentTypeEnum;
import com.whoamizq.codebbs.codebbs.exception.CustomizeErrorCode;
import com.whoamizq.codebbs.codebbs.exception.CustomizeException;
import com.whoamizq.codebbs.codebbs.model.Question;
import com.whoamizq.codebbs.codebbs.model.User;
import com.whoamizq.codebbs.codebbs.service.CommentService;
import com.whoamizq.codebbs.codebbs.service.QuestionService;
import com.whoamizq.codebbs.codebbs.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisService redisService;

    /**
     * 根据id查询问题返回问题及发起问题的用户数据
     * @param id
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/question/{id}")
//    @RequestMapping(value = "/question/{id}")
    public String question(@PathVariable("id") String id, Model model,
                           HttpSession session){
        Long questionId = null;
        try{
            questionId = Long.parseLong(id);
        }catch (NumberFormatException e){
            throw new CustomizeException(CustomizeErrorCode.INVALID_INPUT);
        }
        QuestionDTO questionDTO = questionService.getById(questionId);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(questionId, CommentTypeEnum.QUESTION);
        //浏览数/阅读数的增加
        questionService.incview(questionId);
        model.addAttribute("question",questionDTO);
        //用户若登陆，便查询该问题下所有评论的点赞关系
        User user = (User)session.getAttribute("user");
        if (user != null && user.getId()!= null){
            for (CommentDTO commentDTO : comments){
                Integer likedStatus = redisService.getLikedStatus(String.valueOf(commentDTO.getId()), String.valueOf(user.getId()));
                commentDTO.setStatus(likedStatus);
            }
        }
        for (CommentDTO commentDTO : comments) {
            Integer likedCount = redisService.getLikedCount(String.valueOf(commentDTO.getId()));
            if (likedCount != null) {
                commentDTO.setLikedCount(likedCount);
            }
        }
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
