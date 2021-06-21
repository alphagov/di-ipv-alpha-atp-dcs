package uk.gov.di.ipv.atp.dcs.services;

import com.google.gson.Gson;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import uk.gov.di.ipv.atp.dcs.domain.ProtectedHeader;
import uk.gov.di.ipv.atp.dcs.domain.Thumbprints;

import java.security.Key;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Map;

@Slf4j
public abstract class AbstractSigningService implements SigningService {

    private final Key clientSigningKey;
    private final Certificate serverSigningCert;
    private final Thumbprints clientSigningThumbprints;
    private final Gson gson;

    public AbstractSigningService(
        Key clientSigningKey,
        Certificate serverSigningCert,
        Thumbprints clientSigningThumbprints,
        Gson gson
    ) {
        this.clientSigningKey = clientSigningKey;
        this.serverSigningCert = serverSigningCert;
        this.clientSigningThumbprints = clientSigningThumbprints;

        this.gson = gson;
    }

    @Override
    public Mono<String> signData(String data) {
        var protectedHeader = ProtectedHeader.builder()
            .algorithm(SignatureAlgorithm.RS256.toString())
            .sha1Thumbprint(clientSigningThumbprints.getSha1Thumbprint())
            .sha256Thumbprint(clientSigningThumbprints.getSha256Thumbprint())
            .build();

        var jsonHeaders = gson.toJson(protectedHeader);
        var jws = Jwts
            .builder()
            .setPayload(data)
            .signWith(clientSigningKey, SignatureAlgorithm.RS256)
            .setHeaderParams(gson.fromJson(jsonHeaders, Map.class))
            .compact();

        return Mono.just(jws);
    }

    @Override
    public Mono<String> unwrapSignature(String data) {
        try {
            var jws = JWSObject.parse(data);
            var verifier = new RSASSAVerifier((RSAPublicKey) serverSigningCert.getPublicKey());

            if (!jws.verify(verifier)) {
                throw new RuntimeException("Failed to verify received JWS");
            }

            return Mono.just(jws.getPayload().toString());
        } catch (ParseException | JOSEException e) {
            return Mono.error(e);
        }
    }
}
