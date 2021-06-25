package uk.gov.di.ipv.atp.dcs.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Controller
public class WelcomeController {

    @GetMapping()
    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_PLAIN)
            .body(BodyInserters.fromValue("Generic ATP Service"));
    }
}
