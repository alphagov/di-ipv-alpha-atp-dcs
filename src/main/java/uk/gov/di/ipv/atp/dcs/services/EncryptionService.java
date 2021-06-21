package uk.gov.di.ipv.atp.dcs.services;

import reactor.core.publisher.Mono;

public interface EncryptionService {

    Mono<String> encrypt(String data);

    Mono<String> decrypt(String data);
}
