package com.bawi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

import static com.bawi.MyRequestUtils.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyGlobalExceptionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyGlobalExceptionController.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleAll(HttpRequestMethodNotSupportedException e, WebRequest request) {
        return getStringResponseEntity(request, e, METHOD_NOT_ALLOWED, "ERROR, Request method not supported - only POST allowed");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception e, WebRequest request) {
        return getStringResponseEntity(request, e, BAD_REQUEST, "ERROR, Request processing failed");
    }

    private ResponseEntity<String> getStringResponseEntity(WebRequest webRequest, Exception e, HttpStatus httpStatus, String message) {
        String errorMessage = MyGlobalExceptionController.class.getSimpleName() + ": " + message + " due to " + e;
        if (webRequest instanceof ServletWebRequest servletWebRequest) {
            HttpServletRequest request = servletWebRequest.getRequest();
            setExceptionAsRequestAttribute(request, e);
            LOGGER.error(errorMessage + " for " + requestInfoWithoutPayload(request), e);
            return ResponseEntity.status(httpStatus).body(message);
        }

        LOGGER.error(errorMessage, e);
        return ResponseEntity.status(httpStatus).body(message);
    }
}
