package com.bawi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MyRequestUtils {
    private static final String EXCEPTION_ATTRIBUTE = "exception";

    public static String requestInfoWithoutPayload(HttpServletRequest request) {
        return "request: servletPath=" + request.getServletPath() +
                ", method=" +  request.getMethod() +
//                ", contentLength=" + request.getContentLength() +
//                ", contentType=" +  request.getContentType() +
//                ", localName=" + request.getLocalName() +
                ", headers=" + filterHeaders(request, h -> true);
    }

    public static String requestAndResponseInfoWithoutPayload(HttpServletRequest request, HttpServletResponse response, long startTime) {
        return "response: status " + response.getStatus() + ", processing time " + (System.currentTimeMillis() - startTime) + " ms, " +
                "exception " + request.getAttribute(EXCEPTION_ATTRIBUTE) + ", " +
                requestInfoWithoutPayload(request);
    }

    public static void setExceptionAsRequestAttribute(HttpServletRequest request, Exception e) {
        request.setAttribute(EXCEPTION_ATTRIBUTE, escapeExceptionAndRootCause(e));
    }

    private static String escapeExceptionAndRootCause(Exception e) {
        Throwable t = e;
        while (t != null && t.getCause() != null) {
            t = t.getCause();
        }
        if (t == null) {
            return "null";
        }
        return (e.getClass().getName() + "__" + t.getClass().getName())
                .replace(".","_")
                .replace(":", "_")
                .replace(" ", "_")
                .replace("'", "_");
    }

    private static Map<String, String> filterHeaders(HttpServletRequest request, Predicate<String> headerNameFilter) {
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

    public static Map<String, String> getHeaders(HttpServletRequest req) {
        return Collections.list(req.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, req::getHeader));
    }

    public static String getRequestInfoWithPayload(byte[] payload, Map<String, String> headers) {
        return "Received " + payload.length + " byte(s) payload " + getPayloadAsStringIfBelow10Chars(payload) +
                " with " + headers.size() + " header(s): " + headers;
    }

    private static String getPayloadAsStringIfBelow10Chars(byte[] payload) {
        return payload.length < 10 ? new String(payload) : "";
    }
}
