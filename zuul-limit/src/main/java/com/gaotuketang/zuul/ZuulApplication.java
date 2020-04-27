package com.gaotuketang.zuul;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableZuulProxy
@SpringCloudApplication
@RestController
public class ZuulApplication {

    public static void main(String[] args) {
//        new SpringApplicationBuilder(ZuulApplication.class).web(true).run(args);
        SpringApplication.run(ZuulApplication.class, args);
    }

//    @Bean
//    public ZuulRateLimitForSingleFilter getSingleFilter(){
//        return new ZuulRateLimitForSingleFilter();
//    }
//
//    @Bean
//    public ZuulRateLimitForEveryServerFilter getServerFilter(){
//        return new ZuulRateLimitForEveryServerFilter();
//    }
//
//    @Bean
//    public SelfDefRateLimitForSingleFilter getSelfRateLimitFilter(){
//        return new SelfDefRateLimitForSingleFilter();
//    }

    @GetMapping("/local/fds/bddd")
    public String forward(){
        return "forward local";
    }
}
