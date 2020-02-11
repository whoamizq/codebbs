package com.whoamizq.codebbs.codebbs.controller;


import com.whoamizq.codebbs.codebbs.dto.PaginationDTO;
import com.whoamizq.codebbs.codebbs.model.User;
import com.whoamizq.codebbs.codebbs.service.NotificationService;
import com.whoamizq.codebbs.codebbs.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action")String action,
                          Model model, HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "5") Integer size){

        User user = (User)request.getSession().getAttribute("user");
        if (user == null){
            return "redirect:/";
        }

        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我发布的问题");
            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        }else if ("replies".equals(action)){
            //通知
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("section","replies");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName","最新回复");
        }
        return "profile";
    }
}
