package uk.gov.di.ipv.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class DcsResponse {

    private final UUID correlationId;
    private final UUID requestId;
    private final boolean error;
    private final boolean valid;
    private final String[] errorMessage;
}
