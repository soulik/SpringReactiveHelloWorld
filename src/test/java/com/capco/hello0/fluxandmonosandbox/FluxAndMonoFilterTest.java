package com.capco.hello0.fluxandmonosandbox;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {
    List<String> mockItems = Arrays.asList("abc", "def", "ghi", "jklm", "op","axyz");

    @Test
    public void filterTest(){
        Flux<String> mockItemsFlux = Flux.fromIterable(mockItems)
                .filter(s -> s.startsWith("a"))
                .log();

        StepVerifier.create(mockItemsFlux)
                .expectNext("abc", "axyz")
                .verifyComplete();
    }

    @Test
    public void filterTestLength(){
        Flux<String> mockItemsFlux = Flux.fromIterable(mockItems)
                .filter(s -> s.length() == 3)
                .log();

        StepVerifier.create(mockItemsFlux)
                .expectNext("abc", "def", "ghi")
                .verifyComplete();
    }
}
