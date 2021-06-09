package uk.gov.di.ipv.services.dcs;

import reactor.core.publisher.Mono;
import uk.gov.di.ipv.domain.DcsCheckRequestDto;

public interface AtpService {

    Mono<String> checkPassportData(DcsCheckRequestDto dto);
}
