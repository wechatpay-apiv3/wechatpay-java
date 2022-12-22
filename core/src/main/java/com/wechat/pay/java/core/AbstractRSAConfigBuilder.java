package com.wechat.pay.java.core;

import com.wechat.pay.java.core.util.PemUtil;
import java.security.PrivateKey;

/** RSAConfigBuilder抽象类 */
public abstract class AbstractRSAConfigBuilder<T extends AbstractRSAConfigBuilder<T>> {

  protected String merchantId;
  protected PrivateKey privateKey;
  protected String merchantSerialNumber;

  protected abstract T self();

  public T merchantId(String merchantId) {
    this.merchantId = merchantId;
    return self();
  }

  public T privateKey(String privateKey) {
    this.privateKey = PemUtil.loadPrivateKeyFromString(privateKey);
    return self();
  }

  public T privateKey(PrivateKey privateKey) {
    this.privateKey = privateKey;
    return self();
  }

  public T privateKeyFromPath(String keyPath) {
    this.privateKey = PemUtil.loadPrivateKeyFromPath(keyPath);
    return self();
  }

  public T merchantSerialNumber(String merchantSerialNumber) {
    this.merchantSerialNumber = merchantSerialNumber;
    return self();
  }
}
