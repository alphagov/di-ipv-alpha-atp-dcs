package uk.gov.di.ipv.atp.dcs.services;

import reactor.core.publisher.Mono;

public interface SigningService {

    Mono<String> signData(String data);

    Mono<String> unwrapSignature(String data);
}
