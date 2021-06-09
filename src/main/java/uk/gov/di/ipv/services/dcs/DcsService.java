package uk.gov.di.ipv.services.dcs;

import reactor.core.publisher.Mono;
import uk.gov.di.ipv.domain.DcsCheckRequestDto;
import uk.gov.di.ipv.domain.DcsPayload;
import uk.gov.di.ipv.domain.DcsResponse;

public interface DcsService {

    DcsPayload createValidPassportRequestPayload(DcsCheckRequestDto dto);

    Mono<DcsResponse> postValidPassportRequest(DcsCheckRequestDto dto);

    Mono<String> wrapRequestPayload(String unwrappedPayload);

    Mono<String> unwrapResponse(String data);
}
