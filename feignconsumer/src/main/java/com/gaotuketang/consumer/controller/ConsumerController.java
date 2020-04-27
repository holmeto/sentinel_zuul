package com.gaotuketang.consumer.controller;


import com.gaotuketang.consumer.client.HelloCilent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Resource
    private HelloCilent client;

    @GetMapping("/test")
    public String verify(){
        return client.getExample();
    }

    @GetMapping("/method")
    public String method1(){
        return "consumer method1";
    }


}
