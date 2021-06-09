package uk.gov.di.ipv.services.dcs;

import reactor.core.publisher.Mono;

public interface SigningService {

    Mono<String> signData(String data);

    Mono<String> unwrapSignature(String data);
}
