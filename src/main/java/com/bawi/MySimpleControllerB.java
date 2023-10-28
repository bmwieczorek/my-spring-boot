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
public class MySimpleControllerB {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySimpleControllerB.class);

    private final MyPayloadWriter myPayloadWriter;

    public MySimpleControllerB(MySimplePayloadWriter myPayloadWriter) {
        this.myPayloadWriter = myPayloadWriter;
    }

    @RequestMapping(path = "postb")
    public ResponseEntity<String> postb(HttpServletRequest request) throws IOException {
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

        @Bean
        MySimplePayloadWriter myPayloadWriterB(@Value("${my-simple-controller-b.my-payload-writer.path}") String path) {
            return new MySimplePayloadWriter(path);
        }
    }
}
