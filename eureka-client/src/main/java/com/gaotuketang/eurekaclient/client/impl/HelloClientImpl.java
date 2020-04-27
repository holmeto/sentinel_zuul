package com.gaotuketang.eurekaclient.client.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@Slf4j
public class HelloClientImpl {

    @GetMapping("/example")
    public String getExample() {
        // Thread.sleep(10000)主要是用来验证熔断
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error("{}线程终止", e);
        }
        return "this is a example";
    }
}
