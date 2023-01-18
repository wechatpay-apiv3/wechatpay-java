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
          + "            \"serial_no\": \"19FBBF2A24A3F3C97F5925FA855A850D6E4624AF\",\n"
          + "            \"effective_time \": \"2018-10-09T21:48:14+08:00\",\n"
          + "            \"expire_time \": \"2023-10-08T21:48:14+08:00\",\n"
          + "            \"encrypt_certificate\": {\n"
          + "                \"algorithm\": \"AEAD_AES_256_GCM\",\n"
          + "                \"nonce\": \"1543b1cd0eb9\",\n"
          + "                \"associated_data\": \"certificate\",\n"
          + "                \"ciphertext\": "
          + "\"5ZQ4y+5Az+AjCgb+bg574jUQvBuQofsPzea3EKpfvvbiREnklJNkzNTVdkAX1SUz1Aaimwcnl8Jjg7R/57L30Gqz"
          + "+QXHBPjer2wo5KwQspwpfkdxWdps9dF+G4VtyBNSqJd1EHlK5ao"
          + "+/x2TsmcZIulBiz3f5civgFMjvtqLnJTPSqRClomJP6CZiNq00GkNJRrsMgND7VD0mzOq3vdt62tgOZNReahDbXOqj4uiHy0sLHWsiWVfOxMR0zV4FXgcvahi1L5yrJdTs4uNX1RQ+chsVAydXCeiiysJbLY/Bz0fwF1NEi0EGrAhO51qUCseGtFBJNJosCSJkn9nW1FqINJtxZ/vUliXlw7IW4gw6mKO2FqijjHe7kWeCT1KukNi8YBa+Z9CPjTBiMEKFx7SasFsAwA6cqu9mMfNGTcDFEIDfbDYGt8XMFWIOH3oOFU2k4cKRBuL9pB7A3netiZ2qUGtYMxkVAxDIz852XKo2P2+YG7TqhijX33qPygTqbo+jKhvj11joL9mHiYsWJi4zBCsxE7yR61lxpix0vnk9v+UuyZAJibVnhd6q9Lo2xFk0fuENYe0PStG5Ue4kdNNe16n+fhK/LaAhIZ5c9nHD7+7GTAifSZNrqzTnpQMuyktkK/DcMrkVPu3Z3N9+ZhcIOp2rP6utItJH52+7kYmOPk92ciMsvCi6wGOaP/kd9CQYchnL1KRjW2OuzGptMAsLOL5rULb3zfh8GGNUD9XUnT56hH6RrCYFrojjDsFzBJFfgTzfgDee+j3EZI1NeMBls2TFrNkHFsbb7XRoxffjnXzP7xoAlDuP/wkF70ASI8mdZf/4Qe0S+8h8M9PWKlQjtS0DJVnOK4ladj9QeV9zB2YycpgiAaMOsQF8mUbP7bssCX+ZZDTJNpmIgPapamb8eGYqnF7MV1R2uPUGBBnF0hcfxaA3iCESzKgeQEOpKIYE+vjXuej3S+bFk8HFd4/hzPNd8vJpaJkxtaETpAhcnmybwg7CZMdBQG6lQxQ8pYcG1ZTsAUFL0BD5QVjTL9U2N9xYGWUDutofZuEcuUqg4z7ljS0c41ORHJrsQKqzwGJGeDl/eKbDTkMkFdFRfEwLhouitJSfjdWQX5ii0KMIZcVLZ8nFq3Znh7VQ41MHg9GOeNQfKTwyI1kATSwZdNYg1lIyFEQ89GSmFBfKT1atBGZa+fo2jaj4GvMrt5bgOHLFF5qn3c3DoITKLD45SVAlvOWTvm3QhaNq89MIvsFdrVsRoSDPz+skNmNmuT6P1YqUnFGr3R8yVQvs6j355OddHOdYyFSdhUVY4sCxzOFdi8yNDs7WKx3H0BcdlNWPHIlb622e+uWtqe4sXO35T52a5tZ/UKlZsacNci6tVoZTbFnNBdV/M673jBKb5dnkr9upKp1O0ixhqrB7xuKuIjP6L4l6qzlpf3pbOv5rNK3Gv2+vpMz0OcguK0QjnIXmb+MJbHZDqE6ffRfhYJHczi3yF0tP9He0+2SB98KeF15HmXMRtgeTmzh/YUFfdKJGNBrV25lO/hK6kZ/cmp/wukTT5F/TIHIevEoMx9YnsDLxCYendxVTcvUf5NkAbRWkpLeAD2aTg5oUwu3yRqgmCGDcUW3E+SzIVar/N6lt7upwb6CUGcXgnF4a8NJM0V7woA/8+xmDdlvAkneZ+jULr2x4FD4W2rmZsM9IjrYyQTc6lQhJ/jDNNO9BpWf7kFiMbea1toeURJRuYYdXsGIfb3suBkuf3WTSoMtJI1UzL1RUuvgSjSGOGmnZhcMbBE4PwA+nvVzTVqbLoxy7sOoglXp251WIWGiVBuvJarCRyM5PYJd8nE6PNYogxp2l4JQpkEHcoRuyyzL/DtzKSJNd2JXCNkIqibsEraNgjUQpRLesEREUJT9iEb05FMBlQpJlcHYbzMT\"\n"
          + "            }\n"
          + "        }\n"
          + "    ]\n"
          + "}\n";

  public static final String DOWNLOAD_CERTIFICATE_SERIAL_NUMBER =
      "19FBBF2A24A3F3C97F5925FA855A850D6E4624AF";

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
