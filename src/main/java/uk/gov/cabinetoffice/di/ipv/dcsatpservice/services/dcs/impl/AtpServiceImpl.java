package uk.gov.cabinetoffice.di.ipv.dcsatpservice.services.dcs.impl;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.domain.DcsCheckRequestDto;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.domain.DcsCheckResponseDto;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.services.dcs.AtpService;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.services.dcs.DcsService;
import uk.gov.cabinetoffice.di.ipv.dcsatpservice.services.dcs.SigningService;

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
