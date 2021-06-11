package com.capco.hello0.router;

import com.capco.hello0.handler.SampleHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RouteFunctionConfig {
    @Bean
    public RouterFunction<ServerResponse> router(SampleHandler handlerFunction){
        return RouterFunctions
                .route(
                        GET("/functional/flux")
                                .and(accept(MediaType.APPLICATION_JSON))
                        ,handlerFunction::flux
                )
                .andRoute(
                        GET("/functional/mono")
                                .and(accept(MediaType.APPLICATION_JSON))
                        ,handlerFunction::mono
                );
    }
}
