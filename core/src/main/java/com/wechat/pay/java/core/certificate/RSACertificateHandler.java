package com.wechat.pay.java.core.certificate;

import com.wechat.pay.java.core.cipher.RSAVerifier;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.util.PemUtil;
import java.security.cert.*;
import java.util.*;

class RSACertificateHandler implements CertificateHandler {

  private static final X509Certificate tenpayCACert =
      PemUtil.loadX509FromString(
          "-----BEGIN CERTIFICATE-----\n"
              + "MIIEcDCCA1igAwIBAgIUG9QiDlDbwEsGrTl1SYRsAcPo69IwDQYJKoZIhvcNAQEL\n"
              + "BQAwcDELMAkGA1UEBhMCQ04xEzARBgNVBAoMCmlUcnVzQ2hpbmExHDAaBgNVBAsM\n"
              + "E0NoaW5hIFRydXN0IE5ldHdvcmsxLjAsBgNVBAMMJWlUcnVzQ2hpbmEgQ2xhc3Mg\n"
              + "MiBFbnRlcnByaXNlIENBIC0gRzMwHhcNMTcwODA5MDkxNTU1WhcNMzIwODA5MDkx\n"
              + "NTU1WjBeMQswCQYDVQQGEwJDTjETMBEGA1UEChMKVGVucGF5LmNvbTEdMBsGA1UE\n"
              + "CxMUVGVucGF5LmNvbSBDQSBDZW50ZXIxGzAZBgNVBAMTElRlbnBheS5jb20gUm9v\n"
              + "dCBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALvnPD6k39BdPYAH\n"
              + "+6lnWPjuHH+2pcmZUf2E8cNFQFNr+ECRZylYV2iKyItCQt3I2/7VIDZl6aR9TE7n\n"
              + "sZrtSmOXCw635QOrq2yF9LTSDotAhf3ER0+216w3age/VzGcNVQpTf6gRCHCuQIk\n"
              + "8pe/oh06JagGvX0wERa+I6NfuG58ZHQY9d6RqLXKQl0Up95v73HDsG487z8k6jcn\n"
              + "qpGngmHQxdWiWRJugqxNRUD+awv2/DUsqGOffPX4jzJ6rLSJSlQXvuniDYxmaiaD\n"
              + "cK0bUbB5aM+1zMwogoHSYxWj/6B+vgcnHQCUrwGdiQR5+F+yRWzy5bO09IzaFgeO\n"
              + "PNPLPOsCAwEAAaOCARIwggEOMBIGA1UdEwEB/wQIMAYBAf8CAQAwDgYDVR0PAQH/\n"
              + "BAQDAgEGMCAGA1UdEQQZMBekFTATMREwDwYDVQQDDAhzd2JlLTI2NjAdBgNVHQ4E\n"
              + "FgQUTFo4GLdm9oHX52HcWnzuL4tui2gwHwYDVR0jBBgwFoAUK1vVxWgI69vN5LA5\n"
              + "MqJf/8dPmEUwRgYDVR0gBD8wPTA7BgoqgRyG7xcBAQECMC0wKwYIKwYBBQUHAgEW\n"
              + "H2h0dHBzOi8vd3d3Lml0cnVzLmNvbS5jbi9jdG5jcHMwPgYDVR0fBDcwNTAzoDGg\n"
              + "L4YtaHR0cDovL3RvcGNhLml0cnVzLmNvbS5jbi9jcmwvaXRydXNjMmNhZzMuY3Js\n"
              + "MA0GCSqGSIb3DQEBCwUAA4IBAQBwZhL/eiOQmMyo1D0IR9mu1DPWl5J3XXhjc4R6\n"
              + "mFgsN/FCeVP9M4U9y2FJH6i5Ha5YCecKGw5pwhA0rjZr/6okWwo22GF+nzI/gQiz\n"
              + "6ugAKs5VjFbeiEb04Ncz4HT8FP1idK3tyCjqCUTkLNt0U3tR7wy26hgOqlT2wCZ9\n"
              + "X4MfT8dUMdt9nCZx4ujN5yZOzaLOCHmzoGDGxgKg91bbu0TG2Yzd2ylhrxxRtFH9\n"
              + "aZ/J1x5UoF7uwhTM8P92DuAldWC1/bX1kciOtQvQEZeAy+9y/1BtFxoBnmDxnqkX\n"
              + "+lirIUYTLDaL7HaLrOLECUlaxZCU/Nkwm3tmqQxtCh+XQBdd\n"
              + "-----END CERTIFICATE-----");

  private static final Set<TrustAnchor> trustAnchor =
      new LinkedHashSet<>(Collections.singletonList(new TrustAnchor(tenpayCACert, null)));

  @Override
  public X509Certificate generateCertificate(String certificate) {
    return PemUtil.loadX509FromString(certificate);
  }

  @Override
  public Verifier generateVerifier(List<X509Certificate> certificateList) {
    return new RSAVerifier(new InMemoryCertificateProvider(certificateList));
  }

  @Override
  public void validateCertPath(X509Certificate certificate) {
    try {
      PKIXParameters params = new PKIXParameters(trustAnchor);
      params.setRevocationEnabled(false);

      List<X509Certificate> certs = new ArrayList<>();
      certs.add(certificate);

      CertificateFactory cf = CertificateFactory.getInstance("X.509");
      CertPath certPath = cf.generateCertPath(certs);

      CertPathValidator validator = CertPathValidator.getInstance("PKIX");
      validator.validate(certPath, params);
    } catch (Exception e) {
      throw new ValidationException(
          String.format("certificate validation failed: %s", e.getMessage()), e);
    }
  }
}
