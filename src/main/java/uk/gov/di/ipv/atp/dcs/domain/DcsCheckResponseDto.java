package uk.gov.di.ipv.atp.dcs.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DcsCheckResponseDto {
    
    private final boolean passportValid;
    private final String[] errorMessages;
}
