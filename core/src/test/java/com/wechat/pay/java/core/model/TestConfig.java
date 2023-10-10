package com.wechat.pay.java.core.model;

import com.wechat.pay.java.core.util.IOUtil;
import com.wechat.pay.java.core.util.PemUtil;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class TestConfig {

  public static final String RESOURCES_DIR;
  public static final String MERCHANT_PRIVATE_KEY_PATH;
  public static final String MERCHANT_CERTIFICATE_PATH;
  public static final String MERCHANT_PRIVATE_KEY_STRING;
  public static final String MERCHANT_CERTIFICATE_STRING;
  public static final String WECHAT_PAY_PRIVATE_KEY_PATH;
  public static final String WECHAT_PAY_CERTIFICATE_PATH;
  public static final String WECHAT_PAY_PRIVATE_KEY_STRING;
  public static final String WECHAT_PAY_CERTIFICATE_STRING;
  public static final PrivateKey MERCHANT_PRIVATE_KEY;
  public static final X509Certificate MERCHANT_CERTIFICATE;
  public static final PrivateKey WECHAT_PAY_PRIVATE_KEY;
  public static final X509Certificate WECHAT_PAY_CERTIFICATE;
  public static final String MERCHANT_CERTIFICATE_SERIAL_NUMBER;
  public static final String WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER;
  public static final String MERCHANT_ID;
  public static final String API_V3_KEY = "a7cde1ZJB1kG2e7VfTs3jQzaWizur8Gb";

  public static final String DOWNLOAD_CERTIFICATE_RESPONSE =
      "{\n"
          + "    \"data\": [\n"
          + "        {\n"
          + "            \"effective_time\": \"2023-09-19T23:15:58+08:00\",\n"
          + "            \"encrypt_certificate\": {\n"
          + "                \"algorithm\": \"AEAD_AES_256_GCM\",\n"
          + "                \"associated_data\": \"certificate\",\n"
          + "                \"ciphertext\": \"Mr/AxbvY+WL9hKlD5nIa03LOMPpn5f+CWeNxE4ujl0f5gXZ9XLJ9CldkArCOTsbU62XgKnTc3YvRMZQ7wCmE2fzwmAenAySTYsPms72JhggPWfdqVxri9YD5wBYKn8Ck0Fc5JIiVw9WlN2oxHBz83mRVsjoTGC5+cX1CdYqKuZ8pkvi0aCJIq07ZC8MHoTowUz+4jYJy9XeKqip83Z7BmUYxsePpNM+HtihBqTJIUYXJfX7Col5X0ACsApRoU9l6swfW/kzvqNpDJavhDBGasLrr5822+Fk5nVkcTi53f8Wbue7ODWjJxj1yhRYcxFCvXLtpYNB+Qa1aXtrBSdBdBI2p7o+6Imn+auHLBCzkrZqF/yd+d4vTn4tqHPLUj89XcHsMf96uBo3T/HQV8LEDeIO+6YvOK9FrIDYsvp+imN4n/mnF7eJKo9mpN/QqWJiawrFpAmMMxFKBjIdpgMnVUl3Z11RcWKyfOJ3kcmItTWcnZFNyDgR9cS0aU6QPlefa3uUW89uzBuHjcFdDl43yU3LE4qZnHLDcDgx4qt0OFCLiy51GXtEpyYs5k99YJBvKK+RyIs4Yc1/smBmClQUhxW+8bsPNOcXfDvH2yX4YzESoO4TV0RqiCgnsSd4zIFyJPiMKIGzVHiw6Kg0eGCmB4mGHEEZCQ0eN3SSPO0QVjjtBYfgXDZfVHSaAB8PjWBVNFimoViZZQrwT6Ygo0LU0Dd8+F5aHFYr4OPHPslr3hrgutEYXDTv1vTA6ABJGqknJ9Y2vtyWpy7aIwxc0kQq/1ezjs+RMVL/LRpGDnbx1/CGPDjZhYgZypKa55+PDhoaQxqkF3xCLz2DGoNHZYmFqxzAOsAK0vJZw4VcAk6ssbA6YIaMuiemEpxfLC9ZiXtRqsX9s+GWjjtA8JywOFMO+NKZm9BH/E5YNg2DDD3sHG9Mciwn7yJ6sdf8h2lAWlEExAOeiBNyshOHAQ0/S+x1ncWNiqY+Uh5n0rtonvePV2MsxVlEWR3AVHn1abcEWkzivd+G5kg7LYO0goqzGClcQfnFfZdO6KwcMXsBCnm9A1Bl4+k5LLokTIYH5/36SRIZ9j0CHGD77BQLE7x8AlNsoFS2m263tLLq78TFqvZUTIZIQcVIl+NInscS3xQbA38qqFqutCWIwoF1azuJ7woiJyMvnS3xzIpNzP+rzJ8VDXxjLjpEUBOC9WDs74+QMtsAdmh6g0KDclHEk8WJ4P2bnso4z3VCGoDT5X/0bS1C+jMZMKaOCNB21FZ08qgUHmKeDeg8ylENu+mh2Y/AaEDSGkGBHIfFbhMYO3uRxhk9hldiHEnHgdkXiVAAUWk/IhtmGijibO/L76vB74Xpes973uK+2iFKgNBk8qTLLZZnuWbcMQuA6VsFULRb/C+q9B21YuAWPMzvgBwr4EX2m/OpF5w8Q0HL7v1H5H0mIGJbrjnklUPoBYrw30W3uk7yF0KE6sfMukafFx0S9rBOL0RykGDPwC0lHSNySkXDeM5XvDJ+dtHK+RDwhJR+azfrWAHGMtZcQ+YVamjWVmHxXM03j3GeCXgqae1upwHzkPL5OwojcQ5MQi3Kv/1xtwY0cRN/yeZTOsXfujtqhkJj/Vltby4lnW4SCj4LjWQF19BY6zhzF4yybBA8bhhtoNP/zx8QkhHAkIROkK2ZVgfocCAPvfQ+vSrsIBO+L5cmg6mzFn4g84lsHscgXnszIlzUrzx/4Tam0uwuS9J1gddZ0I0QUt9SJzfsoBCTU0OgNMUtXKS5HvgXe7abexMtFH5eulOsAuFZkQ5LYJa/m4yHFInXzL/t3hjdTi2NmxUufvYzwIyffRTu5vS9vjscARD9lUQDHFQ/xgWe1i8EkRlrSfjlXt00+4//h2bcJKyTBT3IXwvKNsuY29V9/vU3HkvWKZ99zpSlmcZRuBa1BHbyPEvc0NlqIIoKidxqUvOR64MzudHhAjgWFP+IXojfhS3Gj9OJhBL7s\",\n"
          + "                \"nonce\": \"14b16c03dc1b\"\n"
          + "            },\n"
          + "            \"expire_time\": \"2028-09-17T23:15:58+08:00\",\n"
          + "            \"serial_no\": \"5DEA10EF55877E3CF6C33255C2E7DEFFF7E427EC\"\n"
          + "        }\n"
          + "    ]\n"
          + "}";

  public static final String DOWNLOAD_CERTIFICATE_SERIAL_NUMBER =
      "5DEA10EF55877E3CF6C33255C2E7DEFFF7E427EC";

  static {
    try {
      RESOURCES_DIR = System.getProperty("user.dir") + "/src/test/resources";
      MERCHANT_PRIVATE_KEY_PATH = RESOURCES_DIR + "/merchant_private_key.pem";
      MERCHANT_CERTIFICATE_PATH = RESOURCES_DIR + "/merchant_certificate.pem";
      WECHAT_PAY_PRIVATE_KEY_PATH = RESOURCES_DIR + "/wechat_pay_private_key.pem";
      WECHAT_PAY_CERTIFICATE_PATH = RESOURCES_DIR + "/wechat_pay_certificate.pem";
      MERCHANT_PRIVATE_KEY_STRING = IOUtil.loadStringFromPath(MERCHANT_PRIVATE_KEY_PATH);
      MERCHANT_PRIVATE_KEY = PemUtil.loadPrivateKeyFromString(MERCHANT_PRIVATE_KEY_STRING);
      MERCHANT_CERTIFICATE_STRING = IOUtil.loadStringFromPath(MERCHANT_CERTIFICATE_PATH);
      MERCHANT_CERTIFICATE = PemUtil.loadX509FromString(MERCHANT_CERTIFICATE_STRING);
      WECHAT_PAY_PRIVATE_KEY_STRING = IOUtil.loadStringFromPath(WECHAT_PAY_PRIVATE_KEY_PATH);
      WECHAT_PAY_PRIVATE_KEY = PemUtil.loadPrivateKeyFromString(WECHAT_PAY_PRIVATE_KEY_STRING);
      WECHAT_PAY_CERTIFICATE_STRING = IOUtil.loadStringFromPath(WECHAT_PAY_CERTIFICATE_PATH);
      WECHAT_PAY_CERTIFICATE = PemUtil.loadX509FromString(WECHAT_PAY_CERTIFICATE_STRING);
      MERCHANT_CERTIFICATE_SERIAL_NUMBER = "5F1C72E2A8931B72A2E13AF8DEE92471EB397115";
      WECHAT_PAY_CERTIFICATE_SERIAL_NUMBER = "440024045C4A427599D09BB4E3DE0279F2E813FD";
      MERCHANT_ID = "1234567891";
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
