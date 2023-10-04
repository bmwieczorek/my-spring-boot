package com.bawi;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.annotation.PostConstruct;


@Component
public class MyCommonsRequestLoggingFilter extends CommonsRequestLoggingFilter {

    @PostConstruct
    public void init() {
        setIncludeQueryString(true);
        setIncludePayload(true);
        setMaxPayloadLength(10000);
        setIncludeClientInfo(true);
        setIncludeHeaders(true);
        setAfterMessagePrefix("After request ");
    }
}
