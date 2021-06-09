package uk.gov.cabinetoffice.di.ipv.dcsatpservice.services.dcs;

import reactor.core.publisher.Mono;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.domain.DcsCheckRequestDto;

public interface AtpService {

    Mono<String> checkPassportData(DcsCheckRequestDto dto);
}
