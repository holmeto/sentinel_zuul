package com.gaotuketang.zuul.config;

import org.springframework.context.annotation.Configuration;

/**
 * 导入marcosbarbero相关包，就可以使用包里为我们准备好的限流filter了，这里没有引用这个包
 */
@Configuration
public class ZuulRatelimitConfiguration {

//    需要导入marcosbarbero包
//    @Bean
//    public RateLimiterErrorHandler rateLimitErrorHandler() {
//        return new DefaultRateLimiterErrorHandler() {
//            @Override
//            public void handleSaveError(String key, Exception e) {
//                // custom code
//            }
//
//            @Override
//            public void handleFetchError(String key, Exception e) {
//                // custom code
//            }
//
//            @Override
//            public void handleError(String msg, Exception e) {
//                // custom code
//            }
//        };
//    }
}