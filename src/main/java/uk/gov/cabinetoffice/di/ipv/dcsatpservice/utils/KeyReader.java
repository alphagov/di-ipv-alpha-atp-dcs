package uk.gov.cabinetoffice.di.ipv.dcsatpservice.utils;

import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public abstract class KeyReader {

    public static Key loadKeyFromFile(String path) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        var factory = KeyFactory.getInstance("RSA");
        var cpr = new ClassPathResource(path);

        try (var reader = new BufferedReader(new InputStreamReader(cpr.getURL().openStream()));
             var pemReader = new PemReader(reader)
        ) {
            var privKeySpec = new PKCS8EncodedKeySpec(pemReader.readPemObject().getContent());
            return factory.generatePrivate(privKeySpec);
        }
    }

    public static Certificate loadCertFromFile(String path) throws IOException, CertificateException {
        var factory = CertificateFactory.getInstance("X.509");
        var cpr = new ClassPathResource(path);
        return factory.generateCertificate(cpr.getURL().openStream());
    }
}
