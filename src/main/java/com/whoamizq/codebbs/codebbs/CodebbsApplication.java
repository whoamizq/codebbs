package com.whoamizq.codebbs.codebbs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.whoamizq.codebbs.codebbs.mapper")
public class CodebbsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodebbsApplication.class, args);
    }

}
