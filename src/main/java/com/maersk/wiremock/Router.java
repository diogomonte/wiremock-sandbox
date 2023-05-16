package com.maersk.wiremock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> data(EnergyApi energyApi) {
        return route(GET("/data"), (request -> energyApi.fecthEnergyData()
                .flatMap(data -> ServerResponse.ok().bodyValue(data))));
    }
}
