package com.winds.bm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("com.winds.bm.dao")
public class BlogBmApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BlogBmApplication.class, args);
    }

}
