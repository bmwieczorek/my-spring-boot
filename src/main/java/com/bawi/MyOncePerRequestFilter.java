package com.bawi;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.bawi.MyRequestUtils.requestInfoWithoutPayload;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter(filterName = "MyOncePerRequestFilter", urlPatterns = "/*")
public class MyOncePerRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyOncePerRequestFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,  @NonNull HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("doFilterInternal before filterChain: " + requestInfoWithoutPayload(request));

        filterChain.doFilter(request, response);

        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        LOGGER.info("doFilterInternal after filterChain: took " + timeTaken + " ms, responseStatus: " + response.getStatus() +
                ", " + requestInfoWithoutPayload(request));
    }
}
