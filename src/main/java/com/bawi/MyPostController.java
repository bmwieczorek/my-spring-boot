package com.bawi;

import com.google.common.io.ByteStreams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class MyPostController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyPostController.class);

    private final MyService myService;
    private final MyPayloadWriterProvider myPayloadWriterProvider;
    private final MetricsPublisher metricsPublisher;

    public MyPostController(MyService myService, MyPayloadWriterProvider myPayloadWriterProvider, MetricsPublisher metricsPublisher) {
        this.myService = myService;
        this.myPayloadWriterProvider = myPayloadWriterProvider;
        this.metricsPublisher = metricsPublisher;
    }

    @RequestMapping(path = "post")
    public ResponseEntity<String> post(@RequestBody byte[] payload, @RequestHeader Map<String, String> headers) {
        metricsPublisher.counter(1L);
        String message = getMessage(payload, headers);
        metricsPublisher.counter(1L);
        LOGGER.info(message);
        return ResponseEntity.ok(message);
    }

    @RequestMapping(path = "post2")
    public ResponseEntity<String> post2(HttpServletRequest request) throws IOException {
        try (ServletInputStream inputStream = request.getInputStream()){
            byte[] payload = toByteArray(inputStream);
            String message = getMessage(payload, getHeaders(request));
            message = myService.update(message);
            MyPayloadWriter myPayloadWriter = myPayloadWriterProvider.get();
            myPayloadWriter.write(payload, getHeaders(request));
            metricsPublisher.counter(1L);
            LOGGER.info(message);
            return ResponseEntity.ok(message);
        }
    }

    @RequestMapping(path = "postg")
    public ResponseEntity<String> postg(HttpServletRequest request) throws IOException {
        try (ServletInputStream inputStream = request.getInputStream()){
            byte[] payload = ByteStreams.toByteArray(inputStream); //
            String message = getMessage(payload, getHeaders(request));
            metricsPublisher.counter(1L);
            LOGGER.info(message);
            return ResponseEntity.ok(message);
        }
    }

    @RequestMapping(path = "postc")
    public ResponseEntity<String> postc(HttpServletRequest request) throws IOException {
        LOGGER.info("/postc started");
        try (ServletInputStream inputStream = request.getInputStream()){
            byte[] payload = IOUtils.toByteArray(inputStream);
            String message = getMessage(payload, getHeaders(request));
            metricsPublisher.counter(1L);
            LOGGER.info(message);
            return ResponseEntity.ok(message);
        }
    }

    @RequestMapping(path = "postc2")
    public ResponseEntity<String> postc2(HttpServletRequest request) {
        LOGGER.info("/postc2 started");
        try (ServletInputStream inputStream = request.getInputStream()){
            byte[] payload = IOUtils.toByteArray(inputStream);
            String message = getMessage(payload, getHeaders(request));
            metricsPublisher.counter(1L);
            LOGGER.info(message);
            return ResponseEntity.ok(message);
        }
        // curl -i -v http://192.168.0.25:8080/postc2 --data-binary @liniatura.pdf
        //* Connected to 192.168.0.25 (192.168.0.25) port 8080 (#0)
        //> Expect: 100-continue
        //* Mark bundle as not supporting multiuse
        //HTTP/1.1 100
        //
        //^C
        catch (Exception e) {
            LOGGER.error("Failed to process", e);
            return ResponseEntity.badRequest().body("Failed to process due " + e.getMessage());
        }
    }

    private static byte[] toByteArray(ServletInputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int n;
        while ((n = inputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, n);
        }
        outputStream.flush();
        return outputStream.toByteArray();
    }

    private static Map<String, String> getHeaders(HttpServletRequest req) {
        return Collections.list(req.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, req::getHeader));
    }

    private static String getMessage(byte[] payload, Map<String, String> headers) {
//        return "Received " + payload.length + " byte(s) payload: " + new String(payload) +
        return "Received " + payload.length + " byte(s) payload" +
                " with " + headers.size() + " header(s): " + headers;
    }
}
