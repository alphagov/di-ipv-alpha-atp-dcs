package uk.gov.di.ipv.atp.dcs.config;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
import uk.gov.di.ipv.atp.dcs.domain.Thumbprints;
import uk.gov.di.ipv.atp.dcs.utils.KeyReader;

import java.io.IOException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Configuration
public class AtpConfig {

    private @Value("${dcs.base-url}") String dcsBaseUrl;
    private @Value("${atp.ipv.signing.key}") String ipvSigningKeyPath;
    private @Value("${atp.ipv.signing.cert}") String ipvSigningCertPath;

    @Bean("dcs-web-client")
    WebClient webClient() {
        return WebClient
            .builder()
            .baseUrl(dcsBaseUrl)
            .build();
    }

    @Bean
    @Primary
    Gson gson() {
        return new Gson();
    }

    @Bean("atp-ipv-signing-key")
    Key ipvSigningKey() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        return KeyReader.loadKeyFromFile(ipvSigningKeyPath);
    }

    @Bean("atp-ipv-signing-thumbprints")
    Thumbprints makeThumbprints() throws CertificateException, IOException, NoSuchAlgorithmException {
        var cert = KeyReader.loadCertFromFile(ipvSigningCertPath);
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
