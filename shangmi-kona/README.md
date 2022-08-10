# wechatpay-java-shangmi 国密算法扩展

商密 (ShangMi)，也被习惯称之为国密，是一组由[中国国家密码局](https://www.oscca.gov.cn/)标准化的密码学算法。微信支付 APIv3 在支持 RSA/AES 等算法的基础上，支持了国密算法。

`wechatpay-java-shangmi` 是微信支付提供的 `wechatpay-java` 国密扩展，帮助开发者完成国密的签名、验签、加解密等工作。

## 前置条件

微信支付暂时没有开放国密的使用，请有需求的商户：

+ 联系微信支付技术支持，获取国密接入文档，开通国密能力
+ 申请国密证书，并下载国密的微信支付平台证书
+ 掌握 `wechatpay-java` 的使用方法

## 配置

`wechatpay-java-shangmi` 依赖的国密套件目前只发布在腾讯的 Maven 仓库，使用前需要在包管理配置中引入相应的仓库配置。 

### maven

在 `pom.xml` 中加入以下代码

```xml
   <repositories>
      <repository>
         <id>tencent-public</id>
         <url>https://mirrors.tencent.com/repository/maven/tencent_public</url>
      </repository>
   </repositories>
```

### gralde

在 `build.gradle` 中加入以下代码

```
repositories {
    mavenCentral()
    maven {
        url "https://mirrors.tencent.com/repository/maven/tencent_public"
    }
}
```

## 请求使用商密

使用 `SMConfig` 替代 `RSAConfig` 初始化具体的业务服务，再调用服务提供的业务接口即可。 国密的签名、验签会注入请求的各个环节。

```java
package com.wechat.pay.java.service;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.shangmi.kona.SMConfig;
import com.wechat.pay.java.service.payment.jsapi.JsapiService;
import com.wechat.pay.java.service.payment.jsapi.model.QueryOrderByIdRequest;
import com.wechat.pay.java.service.payment.model.Transaction;


/** 下载微信支付平台证书为例 */
public class QuickStart {

  /** 商户号 */
  public static String merchantId = "";
  /** 商户API私钥路径 */
  public static String privateKeyPath = "";
  /** 商户证书序列号 */
  public static String merchantSerialNumber = "";
  /** 微信支付平台证书路径 */
  public static String wechatPayCertificatePath = "";
  /** 微信支付 APIv3 密钥 */
  public static String apiV3Key = "";

  public static void main(String[] args) {
    Config config =
        new SMConfig.Builder()
            .merchantId(merchantId)
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .wechatPayCertificatesFromPath(wechatPayCertificatePath)
            .build();
    JsapiService service = new JsapiService.Builder().config(config).build();
    QueryOrderByIdRequest request = new QueryOrderByIdRequest();
    // 设置业务参数
    Transaction transaction = service.queryOrderById(request);
  }
}
```

### 通知回调使用国密

使用  `SMNotificationConfig` 初始化 `NotificationParser`，`NotificationParser` 即可处理国密的通知回调。

```java
// 国密 NotificationConfig
NotificationConfig smNotificationConfig = new SMNotificationConfig.Builder()
        .apiV3Key(apiV3Key)
        .certificates(smWechatPayCertificateString)
        .build();

// RSANotificationConfig
NotificationConfig rsaNotificationConfig = new RSANotificationConfig.Builder()
        .apiV3Key(apiV3Key)
        .certificates(rsaWechatPayCertificateString)
        .build();

// 初始化 NotificationParser，支持设置一个或者多个 Config
NotificationParser parser = new NotificationParser(rsaNotificationConfig, smNotificationConfig);
```

## 常见问题

### 请求时能同时使用国密和 RSA 吗？

SDK 支持创建多个 `service`，请使用 `RSAConfig` 和 `SMConfig` 分别构造对应的 `service`。
在发送请求时，再根据业务逻辑选择你希望使用的 `service`。

### 下载微信支付平台证书怎么失败了

开通国密后，微信支付平台证书下载接口会同时返回 RSA 和国密的多份证书。当前实现没有处理多种形式的加密，所以失败了。

微信支付平台证书下载接口可能会有调整，到时 SDK 会在第一时间适配。

### Kona 是什么

Kona 国密套件是由腾讯研发且长期维护的一组 Java 库，它包括了三个组件：

+ kona-cypto 基于 [Java Cryptography Architecture (JCA)](https://en.wikipedia.org/wiki/Java_Cryptography_Architecture) 实现了符合国家标准的国密基础算法。
+ kona-pkix 实现了国密 X.509 证书解析，及其证书链验证。
+ kona-ssl 实现了中国的 TLCP 协议，并遵循 [RFC8998](https://www.rfc-editor.org/rfc/rfc8998.html) 在 TLS 1.3 协议中支持了国密算法。

`wechatpay-java-shangmi` 使用了 `kona-crypto` 和 `kona-pkix`。
