package uk.gov.di.ipv.services.dcs.impl;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uk.gov.di.ipv.domain.Thumbprints;
import uk.gov.di.ipv.services.dcs.AbstractSigningService;

import java.security.Key;
import java.security.cert.Certificate;

@Slf4j
@Service("dcs-signing-service")
public class DcsSigningServiceImpl extends AbstractSigningService {

    public DcsSigningServiceImpl(
        @Qualifier("dcs-client-signing-key") Key dcsClientSigningKey,
        @Qualifier("dcs-server-signing-cert") Certificate dcsServerSigningCert,
        @Qualifier("dcs-client-signing-thumbprints") Thumbprints dcsClientSigningThumbprints,
        Gson gson
    ) {
        super(dcsClientSigningKey, dcsServerSigningCert, dcsClientSigningThumbprints, gson);
    }

    @Override
    public Mono<String> signData(String data) {
        return super.signData(data);
    }

    @Override
    public Mono<String> unwrapSignature(String data) {
        return super.unwrapSignature(data);
    }
}
