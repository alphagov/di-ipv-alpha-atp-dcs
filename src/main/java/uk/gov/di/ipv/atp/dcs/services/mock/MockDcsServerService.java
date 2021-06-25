package uk.gov.di.ipv.atp.dcs.services.mock;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uk.gov.di.ipv.atp.dcs.domain.DcsPayload;
import uk.gov.di.ipv.atp.dcs.domain.DcsResponse;
import uk.gov.di.ipv.atp.dcs.services.EncryptionService;
import uk.gov.di.ipv.atp.dcs.services.SigningService;

@Slf4j
@Service
@Conditional(value = MockCondition.class)
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
