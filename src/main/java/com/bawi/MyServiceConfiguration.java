package com.bawi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("my-service.type")
public class MyServiceConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyServiceConfiguration.class);

    @Bean
    @ConditionalOnProperty(prefix = "my-service", value = "type", havingValue = "real", matchIfMissing = true)
    MyService realService() {
        LOGGER.info("Created real MyService");
        return String::toUpperCase;
    }

    @Bean
    @ConditionalOnProperty(prefix = "my-service", value = "type", havingValue = "stub")
    MyService stubService() {
        LOGGER.info("Created stubbed MyService");
        return message -> {
            LOGGER.info("Stubbing message");
            return message;
        };
    }

}
