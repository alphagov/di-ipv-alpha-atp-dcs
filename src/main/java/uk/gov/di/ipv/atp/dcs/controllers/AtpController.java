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

@Controller
@Slf4j
public class AtpController {

    private final AtpService atpService;

    @Autowired
    public AtpController(AtpService atpService) {
        this.atpService = atpService;
    }

    @PostMapping("/process")
    public Mono<ResponseEntity<String>> process(@RequestBody DcsCheckRequestDto dto) {
        log.info("Processing data");
        var attributes = atpService.checkPassportData(dto);

        return attributes
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
