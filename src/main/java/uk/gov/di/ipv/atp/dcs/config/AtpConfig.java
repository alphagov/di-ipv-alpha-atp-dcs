package uk.gov.di.ipv.atp.dcs.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import uk.gov.di.ipv.atp.dcs.domain.Thumbprints;
import uk.gov.di.ipv.atp.dcs.utils.InstantConverter;
import uk.gov.di.ipv.atp.dcs.utils.KeyReader;

import javax.net.ssl.SSLException;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Base64;

@Slf4j
@Configuration
public class AtpConfig {

    private @Value("${dcs.base-url}") String dcsBaseUrl;
    private @Value("${atp.ipv.signing.key}") String ipvSigningKey;
    private @Value("${atp.ipv.signing.cert}") String ipvSigningCert;

    private @Value("${dcs.tls.client.key}") String tlsClientKey;
    private @Value("${dcs.tls.client.cert}") String tlsClientCert;
    private @Value("${dcs.tls.server.cert}") String tlsServerCert;

    @Bean("dcs-web-client")
    WebClient webClient(@Qualifier("dcs-tls-ssl") SslContext sslContext) {
        var strippedBaseUrl = dcsBaseUrl.replaceAll("\"", "");

        var connector = HttpClient.create().secure(spec -> spec.sslContext(sslContext));
        var reactorConnector = new ReactorClientHttpConnector(connector);

        return WebClient
            .builder()
            .clientConnector(reactorConnector)
            .baseUrl(strippedBaseUrl)
            .build();
    }

    @Bean("dcs-tls-ssl")
    SslContext sslContext() throws SSLException, NoSuchAlgorithmException, InvalidKeySpecException, CertificateException {
        var clientKey = KeyReader.loadKey(tlsClientKey);
        var clientCert = KeyReader.loadCertFromString(tlsClientCert);
        var serverCert = KeyReader.loadCertFromString(tlsServerCert);

        return SslContextBuilder
            .forClient()
            .keyManager((PrivateKey) clientKey, (X509Certificate) clientCert)
//            .trustManager((X509Certificate) serverCert)
            .trustManager(InsecureTrustManagerFactory.INSTANCE) // TODO: Remove this
            .build();
    }

    @Bean
    @Primary
    Gson gson() {
        return new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantConverter())
            .create();
    }

    @Bean("atp-ipv-signing-key")
    Key ipvSigningKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyReader.loadKey(ipvSigningKey);
    }

    @Bean("atp-ipv-signing-thumbprints")
    Thumbprints makeThumbprints() throws CertificateException, NoSuchAlgorithmException {
        var cert = KeyReader.loadCertFromString(ipvSigningCert);
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
