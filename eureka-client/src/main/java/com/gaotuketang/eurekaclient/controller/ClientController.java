package com.gaotuketang.eurekaclient.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@Slf4j
public class ClientController {

    @GetMapping("/test")
    public String test(){
        return "hello world";
    }
}
