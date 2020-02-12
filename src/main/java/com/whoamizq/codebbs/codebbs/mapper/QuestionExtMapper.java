package com.whoamizq.codebbs.codebbs.mapper;

import com.whoamizq.codebbs.codebbs.dto.QuestionQueryDTO;
import com.whoamizq.codebbs.codebbs.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);

    int incCommentCount(Question record);

    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);

}
