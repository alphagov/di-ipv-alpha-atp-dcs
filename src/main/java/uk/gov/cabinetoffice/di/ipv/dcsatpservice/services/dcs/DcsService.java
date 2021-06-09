package uk.gov.cabinetoffice.di.ipv.dcsatpservice.services.dcs;

import reactor.core.publisher.Mono;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.domain.DcsCheckRequestDto;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.domain.DcsPayload;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.domain.DcsResponse;

public interface DcsService {

    DcsPayload createValidPassportRequestPayload(DcsCheckRequestDto dto);

    Mono<DcsResponse> postValidPassportRequest(DcsCheckRequestDto dto);

    Mono<String> wrapRequestPayload(String unwrappedPayload);

    Mono<String> unwrapResponse(String data);
}
