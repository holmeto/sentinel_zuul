package com.gaotuketang.consumer.client;

import org.springframework.stereotype.Component;

@Component
public class HelloClientFallBack implements HelloCilent {
    @Override
    public String getExample() {
        return "服务方长时间无响应，被熔断了";
    }
}
