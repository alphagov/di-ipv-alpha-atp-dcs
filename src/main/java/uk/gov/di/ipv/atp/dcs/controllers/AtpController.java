package uk.gov.di.ipv.atp.dcs.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;
import uk.gov.di.ipv.atp.dcs.domain.DcsCheckRequestDto;
import uk.gov.di.ipv.atp.dcs.services.AtpService;
import uk.gov.di.ipv.atp.dcs.services.mock.MockDcsServerService;

@Controller
@Slf4j
public class AtpController {

    private final AtpService atpService;
    private final MockDcsServerService mockServer;

    @Autowired
    public AtpController(AtpService atpService, MockDcsServerService mockServer) {
        this.atpService = atpService;
        this.mockServer = mockServer;
    }

    @PostMapping("/process")
    public Mono<ResponseEntity<String>> process(@RequestBody DcsCheckRequestDto dto) {
        log.info("Processing data");
        var attributes = atpService.checkPassportData(dto);

        return attributes
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping("/checks/passport")
    public Mono<ResponseEntity<String>> check(@RequestBody String jws) {
        var mockedResponse = mockServer.mockDcs(jws);

        return mockedResponse
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
