package uk.gov.cabinetoffice.di.ipv.dcsatpservice.services.dcs.mock;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.domain.Thumbprints;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.services.dcs.AbstractSigningService;

import java.security.Key;
import java.security.cert.Certificate;

@Service("mock-server-signing-service")
@Slf4j
public class DcsMockServerSigningServiceImpl extends AbstractSigningService {

    public DcsMockServerSigningServiceImpl(
        @Qualifier("dcs-mock-server-signing-key") Key dcsClientSigningKey,
        @Qualifier("dcs-mock-client-signing-cert") Certificate dcsServerSigningCert,
        @Qualifier("dcs-mock-server-signing-thumbprints") Thumbprints dcsClientSigningThumbprints,
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
