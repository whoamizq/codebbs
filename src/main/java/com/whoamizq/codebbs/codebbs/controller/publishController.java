package com.whoamizq.codebbs.codebbs.controller;

import com.whoamizq.codebbs.codebbs.cache.TagCache;
import com.whoamizq.codebbs.codebbs.dto.QuestionDTO;
import com.whoamizq.codebbs.codebbs.model.Question;
import com.whoamizq.codebbs.codebbs.model.User;
import com.whoamizq.codebbs.codebbs.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class publishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id,Model model){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "description")String description,
            @RequestParam(value = "tag")String tag,
            HttpServletRequest request, Model model,
            @RequestParam(value = "id")Long id){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags",TagCache.get());
        //校验输入是否为空
        if (title == null || title.equals("")) {
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (description == null || description.equals("")) {
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if (tag == null || tag.equals("")) {
            model.addAttribute("error","请至少填写一个标签");
            return "publish";
        }
        //校验标签的合法性
        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)){
            model.addAttribute("error","输入非法标签"+invalid);
            return "publish";
        }

        //校验登录
        User user = (User)request.getSession().getAttribute("user");
        if (user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
