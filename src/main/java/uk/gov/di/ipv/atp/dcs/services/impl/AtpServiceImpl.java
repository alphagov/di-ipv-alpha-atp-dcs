package uk.gov.di.ipv.atp.dcs.services.impl;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uk.gov.di.ipv.atp.dcs.domain.DcsCheckRequestDto;
import uk.gov.di.ipv.atp.dcs.domain.DcsCheckResponseDto;
import uk.gov.di.ipv.atp.dcs.services.AtpService;
import uk.gov.di.ipv.atp.dcs.services.DcsService;
import uk.gov.di.ipv.atp.dcs.services.SigningService;

@Service
public class AtpServiceImpl implements AtpService {

    private final DcsService dcsService;
    private final SigningService signingService;
    private final Gson gson;

    @Autowired
    public AtpServiceImpl(
        DcsService dcsService,
        @Qualifier("atp-ipv-signing-service") SigningService signingService,
        Gson gson
    ) {
        this.dcsService = dcsService;
        this.signingService = signingService;
        this.gson = gson;
    }

    @Override
    public Mono<String> checkPassportData(DcsCheckRequestDto dto) {
        return dcsService.postValidPassportRequest(dto)
            .flatMap(dcsResponse -> Mono.just(DcsCheckResponseDto.builder()
                .passportValid(dcsResponse.isValid())
                .errorMessages(dcsResponse.getErrorMessage())
                .build()))
            .flatMap(responseDto -> Mono.just(gson.toJson(responseDto)))
            .flatMap(signingService::signData);
    }
}
