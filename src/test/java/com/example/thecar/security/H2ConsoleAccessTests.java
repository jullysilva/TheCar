package com.example.thecar.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

// O console H2 é um servlet próprio, por isso precisa de um servidor real (MockMvc só cobre o DispatcherServlet)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class H2ConsoleAccessTests {

    @Autowired private TestRestTemplate restTemplate;

    @Test
    void h2ConsoleIsPublicAndAllowsSameOriginFrames() {
        ResponseEntity<String> response = restTemplate.getForEntity("/h2-console/", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getFirst("X-Frame-Options")).isEqualTo("SAMEORIGIN");
    }
}
