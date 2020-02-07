package com.whoamizq.codebbs.codebbs.dto;

import lombok.Data;

/**
 * github用户对象属性
 */
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;


}
