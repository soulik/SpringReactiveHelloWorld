package com.capco.hello0.fluxandmonosandbox;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.test.StepVerifier;

//import java.util.concurrent.Flow;

public class FluxAndMonoBackPressureTest {
    @Test
    public void backPressureTest(){
        Flux<Integer> mockFlux = Flux.range(1, 10)
                .log();

        StepVerifier.create(mockFlux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();

    }

    @Test
    public void backPressure(){
        Flux<Integer> mockFlux = Flux.range(1, 10)
                .log();

        //mockFlux.subscribeWith(FluxProcessor.)

        mockFlux.subscribe((elm) -> System.out.println("Element: " + elm)
                ,(e) -> System.err.println("Exception occured: " + e)
                ,() -> System.out.println("Done.")
                ,(sub) -> sub.request(2));
    }

    @Test
    public void backPressure_withCancel(){
        Flux<Integer> mockFlux = Flux.range(1, 10)
                .log();

        //mockFlux.subscribeWith(FluxProcessor.)

        mockFlux.subscribe((elm) -> System.out.println("Element: " + elm)
                ,(e) -> System.err.println("Exception occured: " + e)
                ,() -> System.out.println("Done.")
                ,(sub) -> sub.cancel());
    }

    @Test
    public void customizedBackPressure_withCancel(){
        Flux<Integer> mockFlux = Flux.range(1, 10)
                .log();

        //mockFlux.subscribeWith(FluxProcessor.)

        mockFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println("Received: " + value);
                if (value == 4){
                    cancel();
                }
            }
        });
    }
}
