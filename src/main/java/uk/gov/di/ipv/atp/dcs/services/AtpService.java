package uk.gov.di.ipv.atp.dcs.services;

import reactor.core.publisher.Mono;
import uk.gov.di.ipv.atp.dcs.domain.DcsCheckRequestDto;

public interface AtpService {

    Mono<String> checkPassportData(DcsCheckRequestDto dto);
}
