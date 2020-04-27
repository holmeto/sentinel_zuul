package com.gaotuketang.zuul.tool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BaseTokenRateLimiter {

    // 当前令牌数
    private AtomicInteger currentTokenNum = new AtomicInteger();

    // 上一次更新更新令牌数的时间，初始是0
    private AtomicLong latestUpdateTime = new AtomicLong();

    // 令牌桶原始容量
    private int bucketSize;

    // 令牌生成速率，单位：个/秒
    private int tokenProduceRate;

    // 限流类型，是秒，还是分
    private long rateLimitType;

    public BaseTokenRateLimiter(int bucketSize, int tokenProduceRate, TimeUnit averageRateUnit) {
        this.bucketSize = bucketSize;
        this.tokenProduceRate = tokenProduceRate;
        switch (averageRateUnit) {
            case SECONDS:
                this.rateLimitType = 1000;
                break;
            case MINUTES:
                this.rateLimitType = 60 * 1000;
                break;
            default:
                this.rateLimitType = 1000;
        }
    }

    /**
     * 获取令牌
     * @return true则获取到令牌，可以访问，false没有获取到，进行限流
     */
    public boolean acquireToken(){
        return acquireToken(System.currentTimeMillis());
    }

    private boolean acquireToken(long curentTimeMillis){
        // 如果容量和生成速率小于等于0，那么不符合逻辑，直接不限流
        if(this.bucketSize <= 0 || this.tokenProduceRate <= 0){
            return true;
        }
        refreshBucket(curentTimeMillis);
        return this.consumeToken();

    }

    /**
     * 刷新令牌
     * @param curentTimeMillis
     */
    public void refreshBucket(long curentTimeMillis){
        // 得到上一次刷新令牌的时间
        long lastTime = this.latestUpdateTime.get();
        long addTime = curentTimeMillis - lastTime;
        // 得到到当前时间段内令牌数理论增加量
        long addedToken = addTime * this.tokenProduceRate / this.rateLimitType;
        // 如果增量大于0，则需要刷新令牌数，同时刷新更新时间
        if(addedToken > 0){
            if (this.latestUpdateTime.compareAndSet(lastTime, curentTimeMillis)){
                while(true){
                    int tokenNum = currentTokenNum.get();
                    int realNum = Math.min(currentTokenNum.get(), this.bucketSize);
                    int remainedNum = (int)Math.max(0, realNum - addedToken);
                    if(this.currentTokenNum.compareAndSet(tokenNum, remainedNum)){
                        return;
                    }
                }
            }
        }
    }

    /**
     * 每次消费一个令牌
     * @return true消费成功。false桶已满，无法消费
     */
    private boolean consumeToken(){
        while(true){
            int currentNum = this.currentTokenNum.get();
            if(currentNum >= this.bucketSize){
                return false;
            }
            if(this.currentTokenNum.compareAndSet(currentNum, currentNum + 1)){
                return true;
            }
        }
    }

    public void resetBucket(){
        this.currentTokenNum.set(0);
        this.latestUpdateTime.set(0);
    }













}
