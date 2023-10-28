package com.bawi;

import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.bawi.MyRequestUtils.getHeaders;
import static com.bawi.MyRequestUtils.getRequestInfoWithPayload;

@RestController
public class MySimpleControllerA {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySimpleControllerA.class);

    public interface MyPayloadWriterA extends MyPayloadWriter {} // requires specific interface if multiple @beans created of the same type

    private final MyPayloadWriter myPayloadWriter;

    public MySimpleControllerA(MyPayloadWriterA myPayloadWriter) {
        this.myPayloadWriter = myPayloadWriter;
    }

    @RequestMapping(path = "posta")
    public ResponseEntity<String> posta(HttpServletRequest request) throws IOException {
        try (ServletInputStream inputStream = request.getInputStream()){
            byte[] payload = ByteStreams.toByteArray(inputStream); //
            String message = getRequestInfoWithPayload(payload, getHeaders(request));
            myPayloadWriter.write(payload, getHeaders(request));
            LOGGER.info(message);
            return ResponseEntity.ok(message);
        }
    }

    @Configuration
    public static class MyConfiguration {
        private static final Logger LOGGER = LoggerFactory.getLogger(MyConfiguration.class);

        @Bean
        MyPayloadWriterA myPayloadWriterA(@Value("${my-simple-controller-a.my-payload-writer.path}") String path) {
            LOGGER.info("Created real MyPayloadWriter for " + path);
            return (payload, headers) -> LOGGER.info("Writing payload and headers to " + path);
        }

    }
}
