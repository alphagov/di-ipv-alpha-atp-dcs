package uk.gov.cabinetoffice.di.ipv.dcsatpservice.services.dcs;

import reactor.core.publisher.Mono;

public interface EncryptionService {

    Mono<String> encrypt(String data);

    Mono<String> decrypt(String data);
}
