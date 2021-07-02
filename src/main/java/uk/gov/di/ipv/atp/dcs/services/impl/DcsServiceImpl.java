package uk.gov.di.ipv.atp.dcs.services.impl;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uk.gov.di.ipv.atp.dcs.domain.DcsCheckRequestDto;
import uk.gov.di.ipv.atp.dcs.domain.DcsPayload;
import uk.gov.di.ipv.atp.dcs.domain.DcsResponse;
import uk.gov.di.ipv.atp.dcs.services.DcsService;
import uk.gov.di.ipv.atp.dcs.services.EncryptionService;
import uk.gov.di.ipv.atp.dcs.services.SigningService;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class DcsServiceImpl implements DcsService {

    private final EncryptionService encryptionService;
    private final SigningService signingService;
    private final WebClient webClient;
    private final Gson gson;

    @Autowired
    public DcsServiceImpl(
        @Qualifier("dcs-encryption-service") EncryptionService encryptionService,
        @Qualifier("dcs-signing-service") SigningService signingService,
        @Qualifier("dcs-web-client") WebClient webClient,
        Gson gson
        ) {
        this.encryptionService = encryptionService;
        this.signingService = signingService;
        this.webClient = webClient;
        this.gson = gson;
    }

    @Override
    public DcsPayload createValidPassportRequestPayload(DcsCheckRequestDto dto) {
        var correlationId = UUID.randomUUID();
        var requestId = UUID.randomUUID();

        log.info(String.format("Creating new DCS payload (correlationId: %s, requestId: %s)", correlationId, requestId));

        return DcsPayload.builder()
            .correlationId(correlationId)
            .requestId(requestId)
            .timestamp(Instant.now())
            .passportNumber(dto.getPassportNumber())
            .surname(dto.getSurname())
            .forenames(dto.getForenames())
            .dateOfBirth(dto.getDateOfBirth())
            .expiryDate(dto.getExpiryDate())
            .build();
    }

    @Override
    public Mono<DcsResponse> postValidPassportRequest(DcsCheckRequestDto dto) {
        var dcsPayload = createValidPassportRequestPayload(dto);
        var wrapped = wrapRequestPayload(gson.toJson(dcsPayload));

        var response = webClient.post()
            .uri("/checks/passport")
            .body(BodyInserters.fromPublisher(wrapped, String.class))
            .header("content-type", "application/jose")
            .exchangeToMono(clientResponse -> {
                if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                    log.info(
                        "Received a 200 response from DCS (requestId: {}, correlationId: {})",
                        dcsPayload.getRequestId(),
                        dcsPayload.getCorrelationId()
                    );
                    return clientResponse.bodyToMono(String.class);
                } else if (clientResponse.statusCode().is4xxClientError()) {
                    log.warn(
                        "Received a 4xx response from DCS (requestId: {}, correlationId: {})",
                        dcsPayload.getRequestId(),
                        dcsPayload.getCorrelationId()
                    );
                    return Mono.just("DCS responded with a 4xx error");
                } else {
                    log.error(
                        "Unable to process request (requestId: {}, correlationId: {})",
                        dcsPayload.getRequestId(),
                        dcsPayload.getCorrelationId()
                    );
                    return clientResponse.createException()
                        .flatMap(Mono::error);
                }
            });

        return response
            .flatMap(this::unwrapResponse)
            .flatMap(unwrapped -> Mono.just(gson.fromJson(unwrapped, DcsResponse.class)));
    }

    @Override
    public Mono<String> wrapRequestPayload(String unwrappedPayload) {
        return signingService.signData(unwrappedPayload)
            .flatMap(encryptionService::encrypt)
            .flatMap(signingService::signData)
            .doOnError(throwable -> { throw new RuntimeException("failed to wrap request payload", throwable); });
    }

    @Override
    public Mono<String> unwrapResponse(String data) {
        return signingService.unwrapSignature(data)
            .flatMap(encryptionService::decrypt)
            .flatMap(signingService::unwrapSignature)
            .doOnError(throwable -> { throw new RuntimeException("failed to unwrap response", throwable); });
    }
}
