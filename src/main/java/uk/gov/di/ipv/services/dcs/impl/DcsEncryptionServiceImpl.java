package uk.gov.di.ipv.services.dcs.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uk.gov.di.ipv.services.dcs.AbstractEncryptionService;

import java.security.Key;
import java.security.cert.Certificate;

@Service("dcs-encryption-service")
public class DcsEncryptionServiceImpl extends AbstractEncryptionService {

    public DcsEncryptionServiceImpl(
        @Qualifier("dcs-client-encryption-key") Key dcsClientEncryptionKey,
        @Qualifier("dcs-server-encryption-cert") Certificate dcsServerEncryptionCert
    ) {
        super(dcsClientEncryptionKey, dcsServerEncryptionCert);
    }

    @Override
    public Mono<String> encrypt(String data) {
        return super.encrypt(data);
    }

    @Override
    public Mono<String> decrypt(String data) {
        return super.decrypt(data);
    }
}
