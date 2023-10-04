package com.bawi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("my-payload.writer")
public class MyPayloadWriterConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyPayloadWriterConfiguration.class);
    private String type = "stub";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        LOGGER.info("Changing type to: {}", type);
        this.type = type;
    }

    @Bean
    public MyPayloadWriterProvider myPayloadWriterProvider(ApplicationContext applicationContext) {
        return () -> {
            String beanName = type + MyPayloadWriter.class.getSimpleName();
            if (!applicationContext.containsBean(beanName)) {
                throw new IllegalArgumentException("Unable to create bean with name " + beanName + " of type " + MyPayloadWriter.class +
                        "Bean name needs to match method name annotated with @Bean creating bean of type " +  MyPayloadWriter.class);
            }
            return applicationContext.getBean(beanName, MyPayloadWriter.class);
        };
    }

    @Bean
    MyPayloadWriter stubMyPayloadWriter() {
        LOGGER.info("Created stub MyPayloadWriter");
        return (payload, headers) -> LOGGER.info("Stubbed writing payload and headers");
    }

    @Bean
    MyPayloadWriter realMyPayloadWriter(@Value("${my-payload.writer.path}") String path) {
        LOGGER.info("Created real MyPayloadWriter for " + path);
        return (payload, headers) -> LOGGER.info("Writing payload and headers to " + path);
    }


}
