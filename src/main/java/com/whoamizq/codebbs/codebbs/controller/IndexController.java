package com.whoamizq.codebbs.codebbs.controller;

import com.whoamizq.codebbs.codebbs.mapper.UserMapper;
import com.whoamizq.codebbs.codebbs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        //访问首页时，查询是否有token，以此来判断展示个人信息
        if (cookies!=null){
            for (Cookie cookie :cookies ) {
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user!=null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
        return "index";
    }
}
