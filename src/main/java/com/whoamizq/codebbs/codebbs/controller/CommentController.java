package com.whoamizq.codebbs.codebbs.controller;

import com.whoamizq.codebbs.codebbs.dto.CommentCreateDTO;
import com.whoamizq.codebbs.codebbs.dto.CommentDTO;
import com.whoamizq.codebbs.codebbs.dto.ResultDTO;
import com.whoamizq.codebbs.codebbs.enums.CommentTypeEnum;
import com.whoamizq.codebbs.codebbs.exception.CustomizeErrorCode;
import com.whoamizq.codebbs.codebbs.model.Comment;
import com.whoamizq.codebbs.codebbs.model.User;
import com.whoamizq.codebbs.codebbs.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value="/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        //校验是否登录
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //校验提交文本是否为空
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment, user);
        return ResultDTO.okOf();
    }

//    @ResponseBody
//    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
//    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
//        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
//        return ResultDTO.okOf(commentDTOS);
//    }
}
