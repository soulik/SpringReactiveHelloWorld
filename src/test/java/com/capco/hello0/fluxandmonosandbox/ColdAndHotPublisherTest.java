package com.capco.hello0.fluxandmonosandbox;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ColdAndHotPublisherTest {
    @Test
    public void coldPublisherTest() throws InterruptedException {
        Flux<String> mockFlux = Flux.just("a","b","c","d","e","f","g","j")
                .delayElements(Duration.ofSeconds(1));

        mockFlux.subscribe(s -> System.out.println("S1 Value: " + s));
        Thread.sleep(2000);

        mockFlux.subscribe(s -> System.out.println("S2 Value: " + s));
        Thread.sleep(5000);

    }

    @Test
    public void hotPublisherTest() throws InterruptedException {
        Flux<String> mockFlux = Flux.just("a","b","c","d","e","f","g","j")
                .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<String> connectFlux = mockFlux.publish();
        connectFlux.connect();

        Thread.sleep(2000);

        connectFlux.subscribe(s -> System.out.println("S1 Value: " + s));
        Thread.sleep(2000);

        connectFlux.subscribe(s -> System.out.println("S2 Value: " + s));
        Thread.sleep(8000);
    }
}
