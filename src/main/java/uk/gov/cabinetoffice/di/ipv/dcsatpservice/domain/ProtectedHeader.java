package uk.gov.cabinetoffice.di.ipv.dcsatpservice.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProtectedHeader {

    @SerializedName("alg")
    String algorithm;

    @SerializedName("x5t")
    String sha1Thumbprint;

    @SerializedName("x5t#S256")
    String sha256Thumbprint;

    @SerializedName("enc")
    String encoding;

    @SerializedName("typ")
    String type;
}
