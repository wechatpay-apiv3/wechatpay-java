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
            + "MIIEFDCCAvygAwIBAgIUXeoQ71WHfjz2wzJVwufe//fkJ+wwDQYJKoZIhvcNAQEL\n"
            + "BQAwXjELMAkGA1UEBhMCQ04xEzARBgNVBAoTClRlbnBheS5jb20xHTAbBgNVBAsT\n"
            + "FFRlbnBheS5jb20gQ0EgQ2VudGVyMRswGQYDVQQDExJUZW5wYXkuY29tIFJvb3Qg\n"
            + "Q0EwHhcNMjMwOTE5MTUxNTU4WhcNMjgwOTE3MTUxNTU4WjBuMRgwFgYDVQQDDA9U\n"
            + "ZW5wYXkuY29tIHNpZ24xEzARBgNVBAoMClRlbnBheS5jb20xHTAbBgNVBAsMFFRl\n"
            + "bnBheS5jb20gQ0EgQ2VudGVyMQswCQYDVQQGDAJDTjERMA8GA1UEBwwIU2hlblpo\n"
            + "ZW4wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC3BqqoSXo3OhSFZzEE\n"
            + "ZwGRqDy59WvxKywe5XaGJh/ohYkSCn8zzCrTOO4QFu112WfT2iXWXeDHSuMgB9XY\n"
            + "9UtVnwel3q7PUnsMZu1Fa7OKVI+SDtIvTSadrWir9BQ0At2ythSB7mbfkqzsnPnm\n"
            + "yJXQk5GnuT/tqRJiLzGXXLbo8muP+vJJXOVPcqu4yLn85+ToeH/tsJVhGDzg0McV\n"
            + "yyGKomEtvq67uH67cYi3+4NsJaI7hkUa15Rj7s2ccEDE792mD1GR2K+oy1m98BHU\n"
            + "RnKmZWNdlAKjZhR+ZLeYoeZcyoqmI7P4G1Vr/yOssXoBImtbLph+G3naJIUdIrWj\n"
            + "Zff3AgMBAAGjgbkwgbYwCQYDVR0TBAIwADALBgNVHQ8EBAMCA/gwgZsGA1UdHwSB\n"
            + "kzCBkDCBjaCBiqCBh4aBhGh0dHA6Ly9ldmNhLml0cnVzLmNvbS5jbi9wdWJsaWMv\n"
            + "aXRydXNjcmw/Q0E9MUJENDIyMEU1MERCQzA0QjA2QUQzOTc1NDk4NDZDMDFDM0U4\n"
            + "RUJEMiZzZz1IQUNDNDcxQjY1NDIyRTEyQjI3QTlEMzNBODdBRDFDREY1OTI2RTE0\n"
            + "MDM3MTANBgkqhkiG9w0BAQsFAAOCAQEAHnjuI/OubrLb2UjYrUJfmv3OWwIacBzQ\n"
            + "jl+0fpPviUkMEXHnwi4sKd5slGK30IeLocfRU+Tl+De7N4PrdiaAswVuMHSbiqPp\n"
            + "0wEkogVqunMDyXX6eBa0ouKavyhbKP169dbbGqbqTgBFL0LuSD7finNbM23BVQ5E\n"
            + "jep2M4Uqz5uDuZuMMiGYqx1cVkit4w196yoPOSgzaBNlKIAwwHICEgnj18oXIskn\n"
            + "l2nzYY/ub+zw78jrkSLec259/Bby2LmhcJNL3Eo2TS0OI95Z6UbnHuHWP60yvPMs\n"
            + "ck9llwSj1J9zyEWrG9TCtwdr38U8VmwIz6RQ9k6CK3Yq8tw/a5pYQQ==\n"
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
