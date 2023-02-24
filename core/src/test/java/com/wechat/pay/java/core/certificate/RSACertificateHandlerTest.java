package com.wechat.pay.java.core.certificate;

import static org.junit.jupiter.api.Assertions.*;

import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.util.PemUtil;
import java.security.cert.X509Certificate;
import org.junit.jupiter.api.Test;

class RSACertificateHandlerTest {

  @Test
  void testValidateCertPath() {
    String validCertificate =
        "-----BEGIN CERTIFICATE-----\n"
            + "MIID8TCCAtmgAwIBAgIUIrqFSZtm0AdWLS62jy3IyrpN7a0wDQYJKoZIhvcNAQEL\n"
            + "BQAwXjELMAkGA1UEBhMCQ04xEzARBgNVBAoTClRlbnBheS5jb20xHTAbBgNVBAsT\n"
            + "FFRlbnBheS5jb20gQ0EgQ2VudGVyMRswGQYDVQQDExJUZW5wYXkuY29tIFJvb3Qg\n"
            + "Q0EwHhcNMTgxMDA5MTM0ODQ0WhcNMjMxMDA4MTM0ODQ0WjCBgjEYMBYGA1UEAwwP\n"
            + "VGVucGF5LmNvbSBzaWduMRMwEQYDVQQKDApUZW5wYXkuY29tMR0wGwYDVQQLDBRU\n"
            + "ZW5wYXkuY29tIENBIENlbnRlcjELMAkGA1UEBgwCQ04xEjAQBgNVBAgMCUd1YW5n\n"
            + "RG9uZzERMA8GA1UEBwwIU2hlblpoZW4wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAw\n"
            + "ggEKAoIBAQCsZQyHPpnVpWFKy1KIaZMH/GlmGTEZVlPoyBeqGgEoygCf+poL17t+\n"
            + "xzJwQyxfcaAG1hG3gLcrB4zQbfkKj8GmnLG8EUUyDw+wQRUvJZVaWX4b36cTLjLV\n"
            + "T0VbNf8l0i6kD+J62GArUArUIo4iKSdihx87hzjNwFQdyKwab/v7Z8G1rbA5pyCq\n"
            + "Q0YnPoBMAkj9ZCGg8Q08pPdZKlnbqjpf7s4ol4PjRI01IpaYcihh29b4px6mste6\n"
            + "fBpSut6zljd67NX/cL6GLbWxIeYXwtcabaYrwooq438mLmv1F1nhJyGoQjdhqRKQ\n"
            + "0k8q2/5tuxOOt01MvFMtZj8Cd1DNVjy5AgMBAAGjgYEwfzAJBgNVHRMEAjAAMAsG\n"
            + "A1UdDwQEAwIE8DBlBgNVHR8EXjBcMFqgWKBWhlRodHRwOi8vZXZjYS5pdHJ1cy5j\n"
            + "b20uY24vcHVibGljL2l0cnVzY3JsP0NBPTFCRDQyMjBFNTBEQkMwNEIwNkFEMzk3\n"
            + "NTQ5ODQ2QzAxQzNFOEVCRDIwDQYJKoZIhvcNAQELBQADggEBAHpWYY0N8HopUQEE\n"
            + "Gc0YkgbCLfP3ZdJpGrwlAD2822PxviNwJNABwe8nONzbLXbD7gzEZ2oCyYRmKhGh\n"
            + "m7q+A1NT+35hStj3fSDgeUyEyG4/qyIA9H4I8V3DLSHyvC15wqD+EDc+2lue4MyJ\n"
            + "D7CAwJDOHqmHif2HsdGgdM0CWYetZPhWTwJcBnTfXNE24IEfE+D/x9ZW2+Q7wiLc\n"
            + "fLdjss9a23EouDFsh5PAhCG8QPBqpxAj6W/JjIuuOE4eFwPfgvp9aq0ELJoUVhje\n"
            + "IGpYaDjj8561zwKhsK4WRcIRTrLPeFaDa8gJjLNFR+5CNXCJIQ3gIIkY5NBho0Uy\n"
            + "HoymOW4=\n"
            + "-----END CERTIFICATE-----";
    X509Certificate certificate = PemUtil.loadX509FromString(validCertificate);

    CertificateHandler handler = new RSACertificateHandler();
    assertDoesNotThrow(
        () -> {
          handler.validateCertPath(certificate);
        });
  }

  @Test
  void testInvalidCertPath() {
    String validCertificate =
        "-----BEGIN CERTIFICATE-----\n"
            + "MIIEhDCCA2ygAwIBAgIUaZrpo6ACKL/hM/qaSpnjHU0lcbQwDQYJKoZIhvcNAQEF\n"
            + "BQAwRjEbMBkGA1UEAwwSVGVucGF5LmNvbSBVc2VyIENBMRIwEAYDVQQLDAlDQSBD\n"
            + "ZW50ZXIxEzARBgNVBAoMClRlbnBheS5jb20wHhcNMjExMTExMDI1NjM2WhcNMjYx\n"
            + "MTEwMDI1NjM2WjCBlTEYMBYGA1UEAwwPVGVucGF5LmNvbSBzaWduMSUwIwYJKoZI\n"
            + "hvcNAQkBFhZzdXBwb3J0QHN6aXRydXMuY29tLmNuMR0wGwYDVQQLDBRUZW5wYXku\n"
            + "Y29tIENBIENlbnRlcjETMBEGA1UECgwKVGVucGF5LmNvbTERMA8GA1UEBwwIU2hl\n"
            + "blpoZW4xCzAJBgNVBAYTAkNOMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKC\n"
            + "AQEA7GvNxHf0MdTI0D4sWjL8fHc2HZ50WzuRwRItt1e7YmLpm4lKjlKNmKLRtrJO\n"
            + "vmTXwqOmJNLKz6QHMnR0az9l5pIk8ZkzurexUpydHwTKs/OOD5ZKTtiaiwYy1kSC\n"
            + "GTVzgEJ7Dw3PzqRMdM80+G30h+RwIDQIXMIZS3W0iLa9pxMXZVzD17N6BiBIpDup\n"
            + "M/yErfWyxBd7jq1crvBoHrbyPh5ag4uiV4E0BptmWbn2nOIMq1vuY/LacozhxPcx\n"
            + "nUVVPKLxWwxppvNQpWrJ0VjCxwgjhFU/DxZuqr50uyB0g4OEGAvlJiX7/l625ded\n"
            + "AJmbiYoJWrOohcqauHdqJaIZ+QIDAQABo4IBGDCCARQwCQYDVR0TBAIwADALBgNV\n"
            + "HQ8EBAMCBsAwTwYIKwYBBQUHAQEEQzBBMD8GCCsGAQUFBzAChjNvY3NwLGh0dHA6\n"
            + "Ly9Zb3VyX1NlcnZlcl9OYW1lOlBvcnQvVG9wQ0EvbG9kcF9CYXNlRE4waQYDVR0f\n"
            + "BGIwYDBeoFygWoZYaHR0cDovLzkuMTkuMTYxLjQ6ODA4MC9Ub3BDQS9wdWJsaWMv\n"
            + "aXRydXNjcmw/Q0E9MzlCNDk3QUJDOEFFODg1NzQ1QkY1NjgxRTRGMDNCOEI2NDdG\n"
            + "MjhFQTAfBgNVHSMEGDAWgBROc805tvupF/jOiYapcvSklvPrLjAdBgNVHQ4EFgQU\n"
            + "SGGfum0liSULBRlrThkdsFe3au4wDQYJKoZIhvcNAQEFBQADggEBAHQBdNMRbLRA\n"
            + "TaBnWvk9InV1R7WaO5uIKk3nx41SvBSiKTyKNKGTgro+1PL9aHPHCmnPZ0tQWSXe\n"
            + "b78mFAmwCrz7LW7L9zQa2K+3Fk/X4A3ESlDpS4VY+xvFmujK7XfmzbqzvR5z/tFe\n"
            + "HAMZ/NMqKc6rah9WcKfRn3EQ0DWfufQmpGPTuX5ZPl84TuPZG7MdApn3Vz4xhxGA\n"
            + "5ohYCoCoBK8YNAcLeHNkmatb6GJfS8U+fVcNdDzbnurISYzJvH15yo1iaGNVAqjP\n"
            + "Fwb9+n3hVZV6Jm1N9VIDgSmAaeBLj3Dm+T0og37FmLQ1cz148OJ+ScVJFjZ3I+9v\n"
            + "IQz4B2jCWH4=\n"
            + "-----END CERTIFICATE-----";
    X509Certificate certificate = PemUtil.loadX509FromString(validCertificate);

    CertificateHandler handler = new RSACertificateHandler();
    assertThrows(
        ValidationException.class,
        () -> {
          handler.validateCertPath(certificate);
        });
  }
}
