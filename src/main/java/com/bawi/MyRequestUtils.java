package com.bawi;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class MyRequestUtils {
    public static String requestInfoWithoutPayload(HttpServletRequest request) {
        return "servletPath=" + request.getServletPath() +
                ", method=" +  request.getMethod() +
                ", contentLength=" + request.getContentLength() +
                ", contentType=" +  request.getContentType() +
                ", localName=" + request.getLocalName() +
                ", headers=" + filterHeaders(request, h -> true);
    }

    public static Map<String, String> filterHeaders(HttpServletRequest request, Predicate<String> headerNameFilter) {
        Map<String, String> newHeaders = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (headerNameFilter.test(headerName)) {
                newHeaders.put(headerName, request.getHeader(headerName));
            }
        }
        return newHeaders;
    }
}
