package uk.gov.cabinetoffice.di.ipv.dcsatpservice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class DcsPayload {

    private final UUID correlationId;
    private final UUID requestId;
    private final Instant timestamp;
    private final String passportNumber;
    private final String surname;
    private final String[] forenames;
    private final Instant dateOfBirth;
    private final Instant expiryDate;
}
