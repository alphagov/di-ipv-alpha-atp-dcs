package uk.gov.di.ipv.atp.dcs.services.impl;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uk.gov.di.ipv.atp.dcs.domain.Thumbprints;
import uk.gov.di.ipv.atp.dcs.services.AbstractSigningService;

import java.security.Key;

@Slf4j
@Service("atp-ipv-signing-service")
public class AtpSigningServiceImpl extends AbstractSigningService {

    public AtpSigningServiceImpl(
        @Qualifier("atp-ipv-signing-key") Key atpIpvSigningKey,
        @Qualifier("atp-ipv-signing-thumbprints") Thumbprints atpIpvSigningThumbprints,
        Gson gson
    ) {
        super(atpIpvSigningKey, null, atpIpvSigningThumbprints, gson);
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
