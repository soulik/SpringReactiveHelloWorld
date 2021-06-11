package com.capco.hello0;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Hello0Router {

    @Bean
    public RouterFunction<ServerResponse> route(HelloHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/hello").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), handler::hello);
    }

    @Bean
    public RouterFunction<ServerResponse> routeApi(HelloHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/api").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::api);
    }
}
