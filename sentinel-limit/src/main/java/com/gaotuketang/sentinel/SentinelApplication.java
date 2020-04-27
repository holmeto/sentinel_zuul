package com.gaotuketang.sentinel;


import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@EnableZuulProxy
@SpringCloudApplication
@RestController
@Slf4j
public class SentinelApplication {

    public static void main(String[] args) {

//        new SpringApplicationBuilder(ZuulApplication.class).web(true).run(args);
        SpringApplication.run(SentinelApplication.class, args);
        initFlowRules();
    }


    public static void execute(String[] args) {
        // 配置规则.
        initFlowRules();

        while (true) {
            // 1.5.0 版本开始可以直接利用 try-with-resources 特性，自动 exit entry
            try (Entry entry = SphU.entry("HelloWorld")) {
                // 被保护的逻辑
                System.out.println("hello world");
            } catch (BlockException ex) {
                // 处理被流控的逻辑
                System.out.println("blocked!");
            }
        }
    }

    @SentinelResource(value = "HelloWorld", blockHandler = "exceptionHandler", fallback = "helloFallback")
    @GetMapping("/sentinel/helloworld")
    public String helloWorld() {
        // 资源中的逻辑
        return "hello world";
    }

    // Fallback 函数，函数签名与原函数一致或加一个 Throwable 类型的参数.
    public String helloFallback() {
        return "Fallback! Halooooo...";
    }

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public String exceptionHandler(BlockException ex) {
        // Do some log here.
        log.error(ex.getRuleLimitApp());
        return "Oops, error occurred";
    }

    private static void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        // resource	资源名，资源名是限流规则的作用对象
        rule.setResource("HelloWorld");
        // grade 限流阈值类型，QPS 模式（1）或并发线程数模式（0）
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // count 限流阈值
        rule.setCount(1);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

}
