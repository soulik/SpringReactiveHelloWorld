package com.capco.hello0.fluxandmonosandbox;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {
    @Test
    public void fluxTest(){
        Flux<String> stringFlux = Flux.just("Abc", "Def", "ghi")
                //.concatWith(Flux.error(new RuntimeException("Just a flesh wound")))
                .concatWith(Flux.just("A message after an exception"))
                .log();

        stringFlux
                .subscribe(System.out::println,
                        (e) -> System.err.println(e),
                        () -> System.out.println("Request completed"));
    }

    @Test
    public void fluxTest_WithoutErrors(){
        Flux<String> stringFlux = Flux.just("Abc", "Def", "ghi")
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Abc")
                .expectNext("Def")
                .expectNext("ghi")
                .verifyComplete();
    }

    @Test
    public void fluxTest_WithoutErrorsInline(){
        Flux<String> stringFlux = Flux.just("Abc", "Def", "ghi")
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Abc", "Def", "ghi")
                .verifyComplete();
    }

    @Test
    public void fluxTest_WithErrors(){
        Flux<String> stringFlux = Flux.just("Abc", "Def", "ghi")
                .concatWith(Flux.error(new RuntimeException("Just a flesh wound")))
                .concatWith(Flux.just("A message after an exception"))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Abc")
                .expectNext("Def")
                .expectNext("ghi")
                //.expectError(RuntimeException.class)
                .expectErrorMessage("Just a flesh wound")
                .verify();
    }

    @Test
    public void fluxTest_NumberOfElements(){
        Flux<String> stringFlux = Flux.just("Abc", "Def", "ghi")
                .concatWith(Flux.error(new RuntimeException("Just a flesh wound")))
                .concatWith(Flux.just("A message after an exception"))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                //.expectError(RuntimeException.class)
                .expectErrorMessage("Just a flesh wound")
                .verify();
    }

    @Test
    public void monoTest(){
        Mono<String> stringMono = Mono.just("abc")
                .log();

        StepVerifier.create(stringMono)
                .expectNext("abc")
                .verifyComplete();
    }

    @Test
    public void monoTest_WithError(){
          StepVerifier.create(Mono.error(new RuntimeException("Just a flesh wound")).log())
                .expectErrorMessage("Just a flesh wound")
                .verify();
    }
}
