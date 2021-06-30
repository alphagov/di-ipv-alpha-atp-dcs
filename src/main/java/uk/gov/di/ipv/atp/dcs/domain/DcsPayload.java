package uk.gov.di.ipv.atp.dcs.domain;

import com.google.gson.annotations.JsonAdapter;
import lombok.Builder;
import lombok.Getter;
import uk.gov.di.ipv.atp.dcs.utils.InstantShortDateAdapter;

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

    @JsonAdapter(InstantShortDateAdapter.class)
    private final Instant dateOfBirth;

    @JsonAdapter(InstantShortDateAdapter.class)
    private final Instant expiryDate;
}
