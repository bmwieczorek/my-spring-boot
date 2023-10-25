package com.bawi;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyHandlerInterceptorConfig implements WebMvcConfigurer {

    private final MyHandlerInterceptor myHandlerInterceptor;

    public MyHandlerInterceptorConfig(MyHandlerInterceptor myHandlerInterceptor) {
        this.myHandlerInterceptor = myHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myHandlerInterceptor);
    }
}
