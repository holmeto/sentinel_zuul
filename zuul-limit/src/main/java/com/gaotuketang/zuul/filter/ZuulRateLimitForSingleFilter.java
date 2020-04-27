package com.gaotuketang.zuul.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;

/**
 * 适用于单节点zuul的限流，并且是对所有服务的限流，对于zuul集群，可以借助redis实现多节点限流
 */

@Slf4j
public class ZuulRateLimitForSingleFilter extends ZuulFilter {

    private final RateLimiter rateLimiter = RateLimiter.create(2);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
//        RequestContext context = RequestContext.getCurrentContext();
//        HttpServletRequest request = context.getRequest();
//        log.info("send {} request to {}", request.getMethod(), request.getRequestURL().toString());
//        Object accessToken = request.getParameter("accessToken");
//        if(accessToken == null){
//            log.warn("accessToken is empty");
//            context.setSendZuulResponse(false);
//            context.setResponseStatusCode(401);
//            return null;
//        }
//        log.info("access token ok");
//        return null;
        try {
            RequestContext currentContext = RequestContext.getCurrentContext();
            HttpServletResponse response = currentContext.getResponse();
            if (!rateLimiter.tryAcquire()) {
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
            log.error("单节点过滤器正在限流：" + e.getMessage());
        }
        return null;
    }
}
