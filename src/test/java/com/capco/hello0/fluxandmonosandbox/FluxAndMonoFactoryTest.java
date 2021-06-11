package com.capco.hello0.fluxandmonosandbox;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {
    List<String> mockItems = Arrays.asList("abc", "def", "ghi", "jklm", "op");

    @Test
    public void fluxUsingIterable(){
        Flux<String> mockItemsFlux = Flux.fromIterable(mockItems)
                .log();
        
        StepVerifier.create(mockItemsFlux)
                .expectNext("abc", "def", "ghi", "jklm", "op")
                .verifyComplete();

    }

    @Test
    public void fluxUsingArray(){
        String[] mockItems = new String[]{"abc", "def", "ghi", "jklm", "op"};

        Flux<String> mockItemsFlux = Flux.fromArray(mockItems)
                .log();

        StepVerifier.create(mockItemsFlux)
                .expectNext("abc", "def", "ghi", "jklm", "op")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream(){
        Flux<String> mockItemsFlux = Flux.fromStream(mockItems.stream())
                .log();

        StepVerifier.create(mockItemsFlux)
                .expectNext("abc", "def", "ghi", "jklm", "op")
                .verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty(){
        Mono<String> mockItemsMono = Mono.justOrEmpty(null);

        StepVerifier.create(mockItemsMono.log())
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier(){
        Supplier<String> stringSupplier = () -> "Hello world";
        Mono<String> mockItemsMono = Mono.fromSupplier(stringSupplier);

        StepVerifier.create(mockItemsMono.log())
                .expectNext("Hello world")
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange(){
        Flux<Integer> mockItemsFlux = Flux.range(1,10);

        StepVerifier.create(mockItemsFlux.log())
                .expectNext(1,2,3,4,5,6,7,8,9,10)
                .verifyComplete();
    }
}
