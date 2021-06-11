package com.capco.hello0.fluxandmonosandbox;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

public class FluxAnaMonoErrorTest {
    @Test
    public void fluxErrorHandling(){
        Flux<String> fluxValues = Flux.just("a","b","c")
                .concatWith(Flux.error(new RuntimeException("This is an exception")))
                .concatWith(Flux.just("d"))
                .onErrorResume((e) -> {
                    System.out.println("Exception occured: " + e);
                    return Flux.just("d1", "d2");
                })
                .log();

        StepVerifier.create(fluxValues)
                .expectSubscription()
                .expectNext("a", "b", "c")
//                .expectError(RuntimeException.class)
//                .verify();
                .expectNext("d1", "d2")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onErrorReturn(){
        Flux<String> fluxValues = Flux.just("a","b","c")
                .concatWith(Flux.error(new RuntimeException("This is an exception")))
                .concatWith(Flux.just("d"))
                .onErrorReturn("empty")
                .log();

        StepVerifier.create(fluxValues)
                .expectSubscription()
                .expectNext("a", "b", "c")
                .expectNext("empty")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onErrorMap(){
        Flux<String> fluxValues = Flux.just("a","b","c")
                .concatWith(Flux.error(new RuntimeException("This is an exception")))
                .concatWith(Flux.just("d"))
                .onErrorMap((e) -> new CustomException(e))
                .log();

        StepVerifier.create(fluxValues)
                .expectSubscription()
                .expectNext("a", "b", "c")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_onErrorMap_withRetry(){
        Flux<String> fluxValues = Flux.just("a","b","c")
                .concatWith(Flux.error(new RuntimeException("This is an exception")))
                .concatWith(Flux.just("d"))
                .onErrorMap((e) -> new CustomException(e))
                .retry(2)
                .log();

        StepVerifier.create(fluxValues)
                .expectSubscription()
                .expectNext("a", "b", "c")
                .expectNext("a", "b", "c")
                .expectNext("a", "b", "c")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_onErrorMap_withRetryBackoff(){
        Flux<String> fluxValues = Flux.just("a","b","c")
                .concatWith(Flux.error(new RuntimeException("This is an exception")))
                .concatWith(Flux.just("d"))
                .onErrorMap((e) -> new CustomException(e))
                /* Note:
                * "retryBackoff" method was moved to separate "Retry" class and it's called with "retryWhen" method!
                * */
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(5)));

        StepVerifier.create(fluxValues.log())
                .expectSubscription()
                .expectNext("a", "b", "c")
                .expectNext("a", "b", "c")
                .expectNext("a", "b", "c")
                .expectError(IllegalStateException.class)
                .verify();
    }

}
