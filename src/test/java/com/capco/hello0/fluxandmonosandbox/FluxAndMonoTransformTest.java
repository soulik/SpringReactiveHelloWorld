package com.capco.hello0.fluxandmonosandbox;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {
    List<String> mockItems = Arrays.asList("abc", "def", "ghi", "jklm", "op");

    @Test
    public void transformUsingMap(){
        Flux<String> mockItemsFlux = Flux.fromIterable(mockItems)
                .map(s -> s.toUpperCase())
                .log();

        StepVerifier.create(mockItemsFlux)
                .expectNext("ABC", "DEF", "GHI", "JKLM", "OP")
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Length(){
        Flux<Integer> mockItemsFlux = Flux.fromIterable(mockItems)
                .map(s -> s.length())
                .log();

        StepVerifier.create(mockItemsFlux)
                .expectNext(3,3,3,4,2)
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Length_WithRepeat(){
        Flux<Integer> mockItemsFlux = Flux.fromIterable(mockItems)
                .map(s -> s.length())
                .repeat(1)
                .log();

        StepVerifier.create(mockItemsFlux)
                .expectNext(3,3,3,4,2,3,3,3,4,2)
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Filter(){
        Flux<String> mockItemsFlux = Flux.fromIterable(mockItems)
                .filter(s -> s.length() >= 4)
                .map(s -> s.toUpperCase())
                .log();

        StepVerifier.create(mockItemsFlux)
                .expectNext("JKLM")
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap(){
        Flux<String> mockItemsFlux = Flux.fromIterable(Arrays.asList("a", "b", "c", "d", "e"))
                .flatMap(s -> {
                    return Flux.fromIterable(convertToList(s));
                })
                .log();

        StepVerifier.create(mockItemsFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }

    @Test
    public void transformUsingFlatMap_usingParallel(){
        Flux<String> mockItemsFlux = Flux.fromIterable(Arrays.asList("a", "b", "c", "d", "e","f"))
                .window(2)// Flux<Flux<String>>
                .flatMap((s) ->
                    s.map(this::convertToList)
                        .subscribeOn(parallel())
                )
                .flatMap(s -> Flux.fromIterable(s))
                .log();

        StepVerifier.create(mockItemsFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap_parallel_ordered(){
        Flux<String> mockItemsFlux = Flux.fromIterable(Arrays.asList("a", "b", "c", "d", "e","f"))
                .window(2)// Flux<Flux<String>>
                //.concatMap((s) ->
                .flatMapSequential((s) ->
                    s.map(this::convertToList)
                        .subscribeOn(parallel())
                )
                .flatMap(s -> Flux.fromIterable(s))
                .log();

        StepVerifier.create(mockItemsFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

}
