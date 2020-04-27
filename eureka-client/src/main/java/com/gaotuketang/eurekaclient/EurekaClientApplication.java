package com.gaotuketang.eurekaclient;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@RestController
public class EurekaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
//        new SpringApplicationBuilder(EurekaClientApplication.class).web(true).run(args);
    }

    @GetMapping("/")
    public String getRoot(){
        return "Welcome, Client Home Page!";
    }

    @GetMapping("/eurekaclient/client/test")
    public String getDefault(){
        return "Welcome, Client Home Page!";
    }

//    @GetMapping("/client/example")
//    public String getExample(){
//        return "Welcome, Client Home Page!";
//    }

}
