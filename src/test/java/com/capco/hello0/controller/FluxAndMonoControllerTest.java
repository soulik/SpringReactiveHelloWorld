package com.capco.hello0.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebFluxTest
public class FluxAndMonoControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void fluxTest1(){
        Flux<Integer> mockFlux = webTestClient.get().uri("/flux1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(mockFlux)
                .expectSubscription()
                .expectNext(1,2,3,4)
                .verifyComplete();
    }

    @Test
    public void fluxTest2(){
        webTestClient.get().uri("/flux1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .hasSize(4);
    }

    @Test
    public void fluxTest3(){
        List<Integer> expectedResult = Arrays.asList(1,2,3,4);

        EntityExchangeResult<List<Integer>> exchangeResult = webTestClient.get().uri("/flux1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();

        assertEquals(expectedResult, exchangeResult.getResponseBody());
    }

    @Test
    public void fluxTest4(){
        List<Integer> expectedResult = Arrays.asList(1,2,3,4);

        webTestClient.get().uri("/flux1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith((response) -> {
                    assertEquals(expectedResult, response.getResponseBody());
                });
    }

    @Test
    public void infiniteFluxTest(){
        Flux<Long> mockFlux = webTestClient.get().uri("/infinite-flux-stream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(mockFlux)
                .expectSubscription()
                .expectNext(0l, 1l, 2l)
                .thenCancel()
                .verify();
    }

    @Test
    public void monoTest(){
        Integer expectedResult = 1;

        webTestClient.get().uri("/mono1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith((response) -> {
                    assertEquals(expectedResult, response.getResponseBody());
                });
    }
}
