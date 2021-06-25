package uk.gov.di.ipv.atp.dcs.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

public abstract class AbstractEncryptionService implements EncryptionService {

    private final Key clientEncryptionKey;
    private final Certificate serverEncryptionCert;

    public AbstractEncryptionService(
        Key clientEncryptionKey,
        Certificate serverEncryptionCert
    ) {
        this.clientEncryptionKey = clientEncryptionKey;
        this.serverEncryptionCert = serverEncryptionCert;
    }

    @Override
    public Mono<String> encrypt(String data) {
        try {
            var header = new JWEHeader
                .Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128CBC_HS256)
                .type(new JOSEObjectType("JWE"))
                .build();
            var jwe = new JWEObject(header, new Payload(data));
            var encrypter = new RSAEncrypter((RSAPublicKey) serverEncryptionCert.getPublicKey());

            jwe.encrypt(encrypter);

            if (!jwe.getState().equals(JWEObject.State.ENCRYPTED)) {
                throw new RuntimeException("Something went wrong, couldn't encrypt JWE");
            }

            return Mono.just(jwe.serialize());
        } catch (JOSEException e) {
            return Mono.error(e);
        }
    }

    @Override
    public Mono<String> decrypt(String data) {
        try {
            var jwe = JWEObject.parse(data);
            var decrypter = new RSADecrypter((PrivateKey) clientEncryptionKey);
            jwe.decrypt(decrypter);

            if (!jwe.getState().equals(JWEObject.State.DECRYPTED)) {
                throw new RuntimeException("Something went wrong, couldn't decrypt JWE");
            }

            return Mono.just(jwe.getPayload().toString());
        } catch (ParseException | JOSEException e) {
            return Mono.error(e);
        }
    }
}
