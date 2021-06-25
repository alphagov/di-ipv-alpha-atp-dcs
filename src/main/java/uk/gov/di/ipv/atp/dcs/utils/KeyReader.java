package uk.gov.di.ipv.atp.dcs.utils;

import java.io.ByteArrayInputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public abstract class KeyReader {

    public static Key loadKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var factory = KeyFactory.getInstance("RSA");
        var stripped = key
            .replaceAll("-----BEGIN PRIVATE KEY----- ", "")
            .replaceAll(" -----END PRIVATE KEY-----", "")
            .replaceAll("\"", "")
            .replaceAll("\\s+", "");
        var decoded = Base64.getDecoder().decode(stripped);
        var privKeySpec = new PKCS8EncodedKeySpec(decoded);
        return factory.generatePrivate(privKeySpec);
    }

    public static Certificate loadCertFromString(String cert) throws CertificateException {
        var factory = CertificateFactory.getInstance("X.509");
        var stripped = cert
            .replaceAll("-----BEGIN CERTIFICATE----- ", "")
            .replaceAll(" -----END CERTIFICATE-----", "")
            .replaceAll("\"", "")
            .replaceAll("\\s+", "");
        var decoded = Base64.getDecoder().decode(stripped);
        return factory.generateCertificate(new ByteArrayInputStream(decoded));
    }
}
