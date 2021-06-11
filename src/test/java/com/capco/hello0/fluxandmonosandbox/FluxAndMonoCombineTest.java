package com.capco.hello0.fluxandmonosandbox;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class FluxAndMonoCombineTest {
    @Test
    public void combineUsingMerge(){
        Flux<String> flux1 = Flux.just("a","b","c");
        Flux<String> flux2 = Flux.just("1","2","3");

        Flux<String> mergedFlux = Flux.merge(flux1, flux2)
                .log();

        StepVerifier.create(mergedFlux)
                .expectSubscription()
                .expectNext("a","b","c", "1","2","3")
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_withDelay(){
        VirtualTimeScheduler.getOrSet();

        Flux<String> flux1 = Flux.just("a","b","c").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("1","2","3").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.merge(flux1, flux2)
                .log();

        /*
        StepVerifier.create(mergedFlux)
                .expectSubscription()
                .expectNextCount(6)
                //.expectNext("a","b","c", "1","2","3")
                .verifyComplete();
         */
        StepVerifier.withVirtualTime(() -> mergedFlux)
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(6))
                .expectNextCount(6)
                .verifyComplete();

    }

    @Test
    public void combineUsingConcat(){
        Flux<String> flux1 = Flux.just("a","b","c").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("1","2","3").delayElements(Duration.ofMillis(500));

        Flux<String> mergedFlux = Flux.concat(flux1, flux2)
                .log();

        StepVerifier.create(mergedFlux)
                .expectSubscription()
                .expectNext("a","b","c", "1","2","3")
                .verifyComplete();
    }

    @Test
    public void combineUsingZip(){
        Flux<String> flux1 = Flux.just("a","b","c").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("1","2","3").delayElements(Duration.ofMillis(500));

        Flux<String> mergedFlux = Flux.zip(flux1, flux2, (elm1, elm2) -> {
            return elm1.concat(elm2);
        })
                .log();

        StepVerifier.create(mergedFlux)
                .expectSubscription()
                .expectNext("a1","b2","c3")
                .verifyComplete();
    }
}
