package com.whoamizq.codebbs.codebbs.dto;

import lombok.Data;

import java.util.List;

/**
 * 标签传输类
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
