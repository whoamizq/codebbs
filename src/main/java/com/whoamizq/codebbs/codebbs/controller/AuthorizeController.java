package com.whoamizq.codebbs.codebbs.controller;

import com.whoamizq.codebbs.codebbs.dto.AccessTokenDTO;
import com.whoamizq.codebbs.codebbs.dto.GithubUser;
import com.whoamizq.codebbs.codebbs.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 认证登录，回调
 */
@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id("0d7350863c9200f3a014");
        accessTokenDTO.setClient_secret("e9ebebf46d665cf982e0848fa98e3bff7bec5c41");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:8081/callback");
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
