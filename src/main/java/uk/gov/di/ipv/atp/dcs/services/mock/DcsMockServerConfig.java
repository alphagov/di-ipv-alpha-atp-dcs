package uk.gov.di.ipv.atp.dcs.services.mock;

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
public class DcsMockServerConfig {

    private @Value("${dcs.server.mock.signing.key}") String serverMockSigningKey;
    private @Value("${dcs.client.signing.cert}") String clientSigningCert;
    private @Value("${dcs.server.mock.encryption.key}") String serverMockEncryptionKey;
    private @Value("${dcs.client.mock.encryption.cert}") String clientEncryptionCert;
    private @Value("${dcs.server.signing.cert}") String serverSigningCert;

    @Bean("dcs-mock-server-signing-key")
    Key mockServerSigningKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyReader.loadKey(serverMockSigningKey);
    }

    @Bean("dcs-mock-server-encryption-key")
    Key mockServerEncryptionKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyReader.loadKey(serverMockEncryptionKey);
    }

    @Bean("dcs-client-encryption-cert")
    Certificate clientEncryptionCertificate() throws CertificateException {
        return KeyReader.loadCertFromString(clientEncryptionCert);
    }

    @Bean("dcs-mock-client-signing-cert")
    Certificate clientSigningCertificate() throws CertificateException {
        return KeyReader.loadCertFromString(clientSigningCert);
    }

    @Bean("dcs-mock-server-signing-thumbprints")
    Thumbprints makeThumbprints() throws CertificateException, NoSuchAlgorithmException {
        var cert = KeyReader.loadCertFromString(serverSigningCert);
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
