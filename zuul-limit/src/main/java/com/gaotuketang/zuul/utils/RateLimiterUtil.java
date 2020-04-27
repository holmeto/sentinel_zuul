package com.gaotuketang.zuul.utils;

import com.gaotuketang.zuul.tool.BaseTokenRateLimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 定义一个限流工具类
 */
public class RateLimiterUtil {

    public static void main(String[] args) {
//        RateLimiterOfToken limiter = new RateLimiterOfToken(TimeUnit.SECONDS);
        BaseTokenRateLimiter limiter = getSelfRateLimit();
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; ++i) {
            pool.execute(() -> {
                while (true) {
                    try {
                        Thread.sleep(((int) (Math.random() * 10)) * 1000);
                        if (!limiter.acquireToken()) {
                            System.err.println(Thread.currentThread().getName() + "限流成功----response.setStatus(404)");
                        } else {
                            System.out.println(Thread.currentThread().getName() + "正常执行");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });


        }
    }


    public static BaseTokenRateLimiter getSelfRateLimit() {
        return new BaseTokenRateLimiter(1, 2, TimeUnit.SECONDS);
    }

}
