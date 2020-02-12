package com.whoamizq.codebbs.codebbs.service;

import com.whoamizq.codebbs.codebbs.dto.CommentDTO;
import com.whoamizq.codebbs.codebbs.enums.CommentTypeEnum;
import com.whoamizq.codebbs.codebbs.enums.NotificationStatusEnum;
import com.whoamizq.codebbs.codebbs.enums.NotificationTypeEnum;
import com.whoamizq.codebbs.codebbs.exception.CustomizeErrorCode;
import com.whoamizq.codebbs.codebbs.exception.CustomizeException;
import com.whoamizq.codebbs.codebbs.mapper.*;
import com.whoamizq.codebbs.codebbs.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 评论回复功能
     * 实现回复问题功能
     * @param comment
     * @param commentator
     */
    @Transactional
    public void  insert(Comment comment, User commentator){
        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType()) ){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);

            // 增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);

//            // 创建通知
            createNotify(comment, dbComment.getCommentator(), commentator.getName(),
                    question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        }else {
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);

            // 创建通知
            createNotify(comment, question.getCreator(), commentator.getName(),
                    question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
    }

    /**
     * 通知消息的创建
     * @param comment
     * @param receiver
     * @param notifierName
     * @param outerTitle
     * @param notificationType
     * @param outerId
     */
    private void createNotify(Comment comment, Long receiver, String notifierName,
                              String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        /**
         * 如果是自己给自己评论不去创建通知
         */
        if (receiver.equals(comment.getCommentator())) {
            return;
        }
        //否则进行通知
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }


    /**
     * 实现回复列表显示功能
     * @param id
     * @return
     */
    public List<CommentDTO> listByTargetId(Long id,CommentTypeEnum typeEnum) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(typeEnum.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0){
            return new ArrayList<>();
        }
        //使用lamda，获取评论用户，去掉重复的用户,使用Java8新特性
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论用户并转换为Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //将comment转换为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;

    }
}
