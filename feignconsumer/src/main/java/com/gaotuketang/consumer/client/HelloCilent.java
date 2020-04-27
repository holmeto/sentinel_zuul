package com.gaotuketang.consumer.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "eurekaclient", fallback = HelloClientFallBack.class)
public interface HelloCilent {

    @GetMapping("/client/example")
    public String getExample();

}
