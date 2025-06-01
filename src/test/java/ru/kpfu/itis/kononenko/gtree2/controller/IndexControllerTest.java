package ru.kpfu.itis.kononenko.gtree2.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.*;
//

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexControllerTest {

    @LocalServerPort
    int port;

    private final TestRestTemplate rest;

    IndexControllerTest(TestRestTemplate rest) {
        this.rest = rest;
    }

    @Test
    void homePageIsReachable() {
        ResponseEntity<String> rsp =
                rest.getForEntity("http://localhost:" + port + "/index", String.class);

        assertThat(rsp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(rsp.getBody()).contains("<html");
    }
}

