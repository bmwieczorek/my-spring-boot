package com.bawi;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "my-service.type=stub"
        })
public class SpringBootPostControllerTest {

    @MockBean
    MetricsPublisher metricsPublisher; // optional

    @Autowired
    TestRestTemplate restTemplate;

    @ParameterizedTest
    @CsvSource({
//            "'/post',  'application/x-www-form-urlencoded'", // failing
            "'/post2',  'application/x-www-form-urlencoded'",
            "'/postg',  'application/x-www-form-urlencoded'",
            "'/postc',  'application/x-www-form-urlencoded'",
    })
//    @EnabledOnOs({OS.WINDOWS, OS.MAC}) // optional
    public void testPost(String path, String contentType) {
        String body = "A A";
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>(Map.of("Content-Type", List.of(contentType)));
        ArgumentCaptor<Long> counterCaptor = ArgumentCaptor.forClass(Long.class);

        ResponseEntity<String> response = restTemplate.postForEntity(path, new HttpEntity<>(body, headers), String.class);
        verify(metricsPublisher).counter(counterCaptor.capture());

        org.junit.jupiter.api.Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        MatcherAssert.assertThat(response.getBody(), CoreMatchers.containsString("A A"));
        org.assertj.core.api.Assertions.assertThat(response.getBody()).contains("A A");
        org.assertj.core.api.Assertions.assertThat(counterCaptor.getValue()).isEqualTo(1L);
    }
}
