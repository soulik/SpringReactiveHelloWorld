package com.capco.hello0.repository;

import com.capco.hello0.document.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository repository;

    List<Item> mockItems = Arrays.asList(
            new Item(null, "Item 1", "Lorem ipsum", 123.0)
            ,new Item(null, "Item 2", "Lorem ipsum dolor sit amet", 456.7)
            ,new Item(null, "Item 3", "blahblah", 7890.0)
            ,new Item("ABC", "Item 4", "blahblah", 70.0)
            ,new Item("DEF", "Item 5", "Lorem ipsum dolor sit amet blahblah", 70.0)
    );

    @Before // Runs before tests
    public void setup(){
        repository.deleteAll()
                .thenMany(Flux.fromIterable(mockItems))
                .flatMap(repository::save)
                .doOnNext((item) -> {
                    System.out.println("Inserted: " + item);
                })
                .blockLast();
    }

    @Test
    public void getAllItems(){
        //Flux<Item> foundItems = repository.findAll();

        StepVerifier.create(repository.findAll())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getItemByID(){
        StepVerifier.create(repository.findById("ABC"))
                .expectSubscription()
                .expectNextMatches((item) ->
                    item.getDescription().equals("Lorem ipsum dolor sit amet blahblah")
                )
                .verifyComplete();
    }

    @Test
    public void getItemByDescription(){
        StepVerifier.create(repository.findByDescription("blahblah").log())
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void saveItem(){
        Item item = new Item(null,"Perm-item 1", "Desc 1", 12.3);
        Mono<Item> savedItem = repository.save(item);

        StepVerifier.create(savedItem.log())
                .expectSubscription()
                .expectNextMatches(
                        (newItem) -> newItem.getId() != null
                                && newItem.getTitle().equals("Perm-item 1")
                                && newItem.getDescription().equals("Desc 1")
                )
                .verifyComplete();
    }

    @Test
    public void updateItem(){
        Flux<Item> updatedItems = repository.findByDescription("blahblah")
                .map(existingItem -> {
                    existingItem.setNumValue(existingItem.getNumValue() * 2.0);
                    return existingItem;
                })
                .flatMap(updatedItem -> {
                    return repository.save(updatedItem);
                });

        StepVerifier.create(updatedItems.log())
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void deleteItemById(){
        Mono<Void> deletedItems = repository.findById("DEF")
                .map(Item::getId)
                .flatMap((id) -> {
                    return repository.deleteById(id);
                });

        StepVerifier.create(deletedItems.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(repository.findAll())
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void deleteItemByObject(){
        Mono<Void> deletedItems = repository.findById("DEF")
                .flatMap((item) -> {
                    return repository.delete(item);
                });

        StepVerifier.create(deletedItems.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(repository.findAll())
                .expectNextCount(4)
                .verifyComplete();
    }
}
