package uk.gov.di.ipv.atp.dcs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;
import uk.gov.di.ipv.atp.dcs.services.mock.MockCondition;
import uk.gov.di.ipv.atp.dcs.services.mock.MockDcsServerService;

@Controller
@Conditional(value = MockCondition.class)
public class MockDcsController {

    private final MockDcsServerService mockServer;

    @Autowired
    public MockDcsController(MockDcsServerService mockServer) {
        this.mockServer = mockServer;
    }

    @PostMapping("/checks/passport")
    public Mono<ResponseEntity<String>> check(@RequestBody String jws) {
        var mockedResponse = mockServer.mockDcs(jws);

        return mockedResponse
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
