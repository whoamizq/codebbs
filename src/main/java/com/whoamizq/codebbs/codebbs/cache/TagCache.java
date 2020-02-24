package com.whoamizq.codebbs.codebbs.cache;

import com.whoamizq.codebbs.codebbs.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签库
 * 可以存入Redis数据库
 * 暂未实现。。。
 */
public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("javascript", "php", "css", "html", "html5", "java",
                "node.js", "python", "c++", "c", "golang", "objective-c", "typescript",
                "shell", "swift", "c#", "sass",  "ruby", "bash", "less", "asp.net", "lua",
                "scala", "coffeescript", "actionscript", "rust", "erlang", "perl"));
        tagDTOS.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("spring", "springboot","bootstrap","laravel",
                "struts", "django", "flask","mybatis","vue","springmvc","shiro",
                "yii", "ruby-on-rails", "tornado", "koa", "express","solr"));
        tagDTOS.add(framework);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("linux", "nginx", "docker", "apache", "ubuntu", "centos",
                "缓存 tomcat", "负载均衡", "unix", "hadoop", "windows-server","zookeeper"));
        tagDTOS.add(server);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql", "redis", "mongodb", "sql", "oracle", "nosql memcached",
                "sqlserver", "postgresql", "sqlite"));
        tagDTOS.add(db);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("git", "github", "visual-studio-code", "vim", "sublime-text",
                "intellij-idea", "eclipse", "maven", "ide", "svn", "visual-studio",
                "atom emacs", "textmate", "hg","微信开发者工具","PyCharm"));
        tagDTOS.add(tool);
        return tagDTOS;
    }

    /**
     * 获取多个标签，去掉逗号
     * @param tags
     * @return
     */
    public static String filterInvalid(String tags) {
        String[] split = StringUtils.split(tags,",");
        List<TagDTO> tagDTOS = get();
        List<String> tagList = tagDTOS.stream()
                .flatMap(tag -> tag.getTags().stream())
                .collect(Collectors.toList());
        String invalid = Arrays.stream(split)
                .filter(t -> StringUtils.isBlank(t) || !tagList.contains(t))
                .collect(Collectors.joining(","));
        return invalid;
    }

//    public static void main(String[] args) {
//        int i= (5-1)>>>1;
//        System.out.println(i);
//    }
}
