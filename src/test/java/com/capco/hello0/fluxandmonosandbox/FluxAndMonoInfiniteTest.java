package com.capco.hello0.fluxandmonosandbox;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoInfiniteTest {

    @Test
    public void infiniteSequence(){
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(200))
                .log();

        infiniteFlux
                .subscribe((elm) -> {
                    System.out.println("Element: " + elm);
                });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void infiniteSequence_withTest(){
        Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .take(3)
                .log();

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();


    }

    @Test
    public void infiniteSequenceMap(){
        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .map((elm) -> elm.intValue())
                .take(3)
                .log();

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }

    @Test
    public void infiniteSequenceMap_WithDelay(){
        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(200))
                .delayElements(Duration.ofSeconds(1))
                .map((elm) -> elm.intValue())
                .take(3)
                .log();

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }

    private Flux<Integer> infiniteSequenceGenerator(int diff){
        return Flux.generate(
                () -> 0L,
                (state, sink) -> {
                    sink.next(state.intValue());
                    return state + diff;
                }
        );
    }

    @Test
    public void infiniteSequence_withGenerator(){
        Flux<Integer> infiniteFlux = infiniteSequenceGenerator(2)
                .take(5)
                .log();

        StepVerifier.create(infiniteFlux)
                .expectSubscription()
                .expectNext(0, 2, 4, 6, 8)
                .verifyComplete();
    }


}
