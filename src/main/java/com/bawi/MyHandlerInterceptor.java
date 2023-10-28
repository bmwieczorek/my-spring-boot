package com.bawi;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.bawi.MyRequestUtils.requestAndResponseInfoWithoutPayload;
import static com.bawi.MyRequestUtils.requestInfoWithoutPayload;

@Component
public class MyHandlerInterceptor implements HandlerInterceptor {
    // added to InterceptorRegistry in MyHandlerInterceptorConfig

    private static final Logger LOGGER = LoggerFactory.getLogger(MyHandlerInterceptor.class);
    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        request.setAttribute(START_TIME, System.currentTimeMillis());
        LOGGER.info("preHandle: " + requestInfoWithoutPayload(request));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        long startTime = (long) request.getAttribute(START_TIME);
        LOGGER.info("afterCompletion: " + requestAndResponseInfoWithoutPayload(request, response, startTime));
    }

}
