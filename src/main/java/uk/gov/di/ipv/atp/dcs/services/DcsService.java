package uk.gov.di.ipv.atp.dcs.services;

import reactor.core.publisher.Mono;
import uk.gov.di.ipv.atp.dcs.domain.DcsCheckRequestDto;
import uk.gov.di.ipv.atp.dcs.domain.DcsPayload;
import uk.gov.di.ipv.atp.dcs.domain.DcsResponse;

public interface DcsService {

    DcsPayload createValidPassportRequestPayload(DcsCheckRequestDto dto);

    Mono<DcsResponse> postValidPassportRequest(DcsCheckRequestDto dto);

    Mono<String> wrapRequestPayload(String unwrappedPayload);

    Mono<String> unwrapResponse(String data);
}
