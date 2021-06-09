package uk.gov.cabinetoffice.di.ipv.dcsatpservice.services.dcs.mock;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.domain.DcsPayload;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.domain.DcsResponse;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.services.dcs.EncryptionService;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.services.dcs.SigningService;

@Slf4j
@Service
public class MockDcsServerService {

    private final EncryptionService mockEncryptionService;
    private final SigningService mockSigningService;
    private final Gson gson;

    public MockDcsServerService(
        @Qualifier("mock-server-encryption-service") EncryptionService mockEncryptionService,
        @Qualifier("mock-server-signing-service") SigningService mockSigningService,
        Gson gson
    ) {
        this.mockEncryptionService = mockEncryptionService;
        this.mockSigningService = mockSigningService;
        this.gson = gson;
    }

    public Mono<String> mockDcs(String payload) {
        log.info("MOCK: Processing mock");

        return mockSigningService.unwrapSignature(payload)
            .flatMap(mockEncryptionService::decrypt)
            .flatMap(mockSigningService::unwrapSignature)
            .flatMap(json -> Mono.just(gson.fromJson(json, DcsPayload.class)))
            .flatMap(dcsPayload -> Mono.just(new DcsResponse(
                dcsPayload.getCorrelationId(),
                dcsPayload.getRequestId(),
                false,
                true,
                null)))
            .doOnSuccess((x) -> log.info(String.format(
                "MOCK: Correlation ID - %s, Request ID - %s",
                x.getCorrelationId().toString(),
                x.getRequestId().toString())))
            .flatMap(dcsResponse -> Mono.just(gson.toJson(dcsResponse)))
            .flatMap(mockSigningService::signData)
            .flatMap(mockEncryptionService::encrypt)
            .flatMap(mockSigningService::signData);
    }
}
