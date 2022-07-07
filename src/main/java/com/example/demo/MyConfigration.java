package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: java编码方式加载Bean
 * @Author: zhouhui2
 * @Date: 2022/3/24 11:17 PM
 */
@Configuration
public class MyConfigration {

    @Bean
    public String getString() {
        return "啦啦";
    }
}
