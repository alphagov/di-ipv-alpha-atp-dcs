package uk.gov.di.ipv.atp.dcs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.di.ipv.atp.dcs.domain.Thumbprints;
import uk.gov.di.ipv.atp.dcs.utils.KeyReader;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Configuration
public class DcsPkiConfig {

    private @Value("${dcs.client.signing.key}") String clientSigningKey;
    private @Value("${dcs.client.signing.cert}") String clientSigningCert;
    private @Value("${dcs.client.encryption.key}") String clientEncryptionKey;
    private @Value("${dcs.server.encryption.cert}") String serverEncryptionCert;
    private @Value("${dcs.server.signing.cert}") String serverSigningCert;

    @Bean("dcs-client-signing-key")
    Key clientSigningKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyReader.loadKey(clientSigningKey);
    }

    @Bean("dcs-client-encryption-key")
    Key clientEncryptionKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyReader.loadKey(clientEncryptionKey);
    }

    @Bean("dcs-server-encryption-cert")
    Certificate serverEncryptionCertificate() throws CertificateException {
        return KeyReader.loadCertFromString(serverEncryptionCert);
    }

    @Bean("dcs-server-signing-cert")
    Certificate serverSigningCertificate() throws CertificateException {
        return KeyReader.loadCertFromString(serverSigningCert);
    }

    @Bean("dcs-client-signing-thumbprints")
    Thumbprints makeThumbprints() throws CertificateException, NoSuchAlgorithmException {
        var cert = KeyReader.loadCertFromString(clientSigningCert);
        return new Thumbprints(
            getThumbprint((X509Certificate) cert, "SHA-1"),
            getThumbprint((X509Certificate) cert, "SHA-256")
        );
    }

    private static String getThumbprint(X509Certificate cert, String hashAlg)
        throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest md = MessageDigest.getInstance(hashAlg);
        byte[] der = cert.getEncoded();
        md.update(der);
        byte[] digest = md.digest();
        return Base64.getUrlEncoder().encodeToString(digest).replaceAll("=", "");
    }
}
