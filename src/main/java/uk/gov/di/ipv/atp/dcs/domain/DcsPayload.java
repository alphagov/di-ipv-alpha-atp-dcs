package uk.gov.di.ipv.atp.dcs.domain;

import lombok.Builder;
import lombok.Getter;

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
