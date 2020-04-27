package com.gaotuketang.zuul.filter;

import com.gaotuketang.zuul.tool.BaseTokenRateLimiter;
import com.gaotuketang.zuul.utils.RateLimiterUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SelfDefRateLimitForSingleFilter extends ZuulFilter {

    private final BaseTokenRateLimiter rateLimiter = RateLimiterUtil.getSelfRateLimit();

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        try {
            RequestContext currentContext = RequestContext.getCurrentContext();
            HttpServletResponse response = currentContext.getResponse();
            if (!rateLimiter.acquireToken()) {
                HttpStatus httpStatus = HttpStatus.TOO_MANY_REQUESTS;
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.setStatus(httpStatus.value());
                response.getWriter().append(httpStatus.getReasonPhrase());
                currentContext.setSendZuulResponse(false);
                throw new ZuulException(
                        httpStatus.getReasonPhrase(),
                        httpStatus.value(),
                        httpStatus.getReasonPhrase()
                );
            }
        } catch (Exception e) {
            log.error("自定义单节点过滤器正在限流：" + e.getMessage());
        }
        return null;
    }
}
