package uk.gov.di.ipv.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Thumbprints {
    private String sha1Thumbprint;
    private String sha256Thumbprint;
}
