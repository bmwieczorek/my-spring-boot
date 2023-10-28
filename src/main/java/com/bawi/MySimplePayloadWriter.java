package com.bawi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MySimplePayloadWriter implements MyPayloadWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySimplePayloadWriter.class);
    private final String path;

    public MySimplePayloadWriter(String path) {
        this.path = path;
        LOGGER.info("Created real MySimplePayloadWriter for " + path);
    }

    @Override
    public void write(byte[] payload, Map<String, String> headers) {
        LOGGER.info("Writing payload and headers to " + path);
    }
}
