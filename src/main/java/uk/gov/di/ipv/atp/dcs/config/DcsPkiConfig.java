package uk.gov.di.ipv.atp.dcs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.di.ipv.atp.dcs.domain.Thumbprints;
import uk.gov.di.ipv.atp.dcs.utils.KeyReader;

import java.io.IOException;
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

    private @Value("${dcs.client.signing.key}") String clientSigningKeyPath;
    private @Value("${dcs.client.signing.cert}") String clientSigningCertPath;
    private @Value("${dcs.client.encryption.key}") String clientEncryptionKeyPath;
    private @Value("${dcs.server.encryption.cert}") String serverEncryptionCertPath;
    private @Value("${dcs.server.signing.cert}") String serverSigningCertPath;

    @Bean("dcs-client-signing-key")
    Key clientSigningKey() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        return KeyReader.loadKeyFromFile(clientSigningKeyPath);
    }

    @Bean("dcs-client-encryption-key")
    Key clientEncryptionKey() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        return KeyReader.loadKeyFromFile(clientEncryptionKeyPath);
    }

    @Bean("dcs-server-encryption-cert")
    Certificate serverEncryptionCertificate() throws IOException, CertificateException {
        return KeyReader.loadCertFromFile(serverEncryptionCertPath);
    }

    @Bean("dcs-server-signing-cert")
    Certificate serverSigningCertificate() throws IOException, CertificateException {
        return KeyReader.loadCertFromFile(serverSigningCertPath);
    }

    @Bean("dcs-client-signing-thumbprints")
    Thumbprints makeThumbprints() throws CertificateException, IOException, NoSuchAlgorithmException {
        var cert = KeyReader.loadCertFromFile(clientSigningCertPath);
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
