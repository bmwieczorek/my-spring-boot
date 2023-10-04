package com.bawi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MetricsPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsPublisher.class);
    public void counter(long cnt) {
        LOGGER.info("Publishing counter: " + cnt);
    }
}
