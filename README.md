# 微信支付 APIv3 Java SDK

[微信支付 APIv3](https://wechatpay-api.gitbook.io/wechatpay-api-v3/) 官方 Java 语言客户端开发库。

开发库由 `core` 和 `service` 组成：
- core 为基础库，包含自动签名和验签的 HTTP 客户端、回调处理、加解密库。
- service 为业务服务，包含[业务接口](service/src/main/java/com/wechat/pay/java/service)和[使用示例](service/src/example/java/com/wechat/pay/java/service)，根据接口契约自动生成，证书服务 `certificate` 和文件服务 `file` 除外。

## 项目状态

当前版本`0.1.0`为测试版本，项目规划详如下。

| 工作项 | 状态 |
| ----- | ---- |
| 证书下载、文件上传 | 已完成 |
| 平台证书自动下载 | 规划中，欢迎 PR |
| 账单下载 | 规划中，欢迎 PR |
| 业务服务（基于接口契约自动生成）| 进行中，有需要请提 issue |
| 其他 HttpClient 适配器 | 有需要请提 issue，欢迎 PR |

## 前置条件

- Java 1.8+。
- [成为微信支付商户](https://pay.weixin.qq.com/index.php/apply/applyment_home/guide_normal)。
- [商户 API 证书](https://wechatpay-api.gitbook.io/wechatpay-api-v3/ren-zheng/zheng-shu#shang-hu-api-zheng-shu)：指由商户申请的，包含商户的商户号、公司名称、公钥信息的证书。
- [商户 API 私钥](https://wechatpay-api.gitbook.io/wechatpay-api-v3/ren-zheng/zheng-shu#shang-hu-api-si-yao)：商户申请商户API证书时，会生成商户私钥，并保存在本地证书文件夹的文件 apiclient_key.pem 中。
- [微信支付平台 API 证书](https://wechatpay-api.gitbook.io/wechatpay-api-v3/ren-zheng/zheng-shu#ping-tai-zheng-shu)：由微信支付负责申请的，包含微信支付平台标识、公钥信息的证书。
- [APIv3 密钥](https://wechatpay-api.gitbook.io/wechatpay-api-v3/ren-zheng/api-v3-mi-yao)：为了保证安全性，微信支付在回调通知和平台证书下载接口中，对关键信息进行了 AES-256-GCM 加密。APIv3 密钥是加密时使用的对称密钥。

## 快速开始

### 安装

最新版本已经在 [Maven Central](https://search.maven.org/artifact/com.github.wechatpay-apiv3/wechatpay-java) 发布。

#### Gradle
在你的 build.gradle 文件中加入如下的依赖
```groovy
implementation 'com.github.wechatpay-apiv3:wechatpay-java:0.1.0'
```

#### Maven
加入以下依赖
```xml
<dependency>
  <groupId>com.github.wechatpay-apiv3</groupId>
  <artifactId>wechatpay-java</artifactId>
  <version>0.1.0</version>
</dependency>
```

### 调用业务请求接口

以下载微信支付平台证书为例，先构建 `config` 和 `service`，再发送请求。详细代码可参考 [QuickStart](service/src/example/java/com/wechat/pay/java/service/QuickStart.java)。

```java
package com.wechat.pay.java.service;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.certificate.CertificateService;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.List;

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
        new RSAConfig.Builder()
            .merchantId(merchantId)
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .wechatPayCertificatesFromPath(wechatPayCertificatePath)
            .build();
    CertificateService certificateService = new CertificateService.Builder().config(config).build();
    List<X509Certificate> certificates =
        certificateService.downloadCertificate(apiV3Key.getBytes(StandardCharsets.UTF_8));
  }
}
```

从示例可见，使用 SDK 并不需要计算请求签名和验证应答签名。

## 示例

### JSAPI 支付下单

请求参数需使用你自己的商户号、证书密钥、AppID 以及对应的 OpenID。

```java
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;

public class JsapiExample {
  public static void main(String[] args) {
    Config config =
        new RSAConfig.Builder()
            .merchantId(merchantId)
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .wechatPayCertificatesFromPath(wechatPayCertificatePath)
            .build();

    JsapiService service = new JsapiService.Builder().config(config).build();

    PrepayRequest request = new PrepayRequest();
    Amount amount = new Amount();
    amount.setTotal(100);
    request.setAmount(amount);
    request.setAppid("wxa9d9651ae******");
    request.setMchid("190000****");
    request.setDescription("测试商品标题");
    request.setNotifyUrl("https://notify_url");
    request.setOutTradeNo("out_trade_no_001");
    Payer payer = new Payer();
    payer.setOpenid("oLTPCuN5a-nBD4rAL_fa********");
    request.setPayer(payer);

    PrepayResponse response = service.prepay(request);
    System.out.println(response.getPrepayId());
  }
}
```

### 查询支付订单

```java
QueryOrderByIdRequest queryRequest = new QueryOrderByIdRequest();
queryRequest.setMchid("190000****");
queryRequest.setTransactionId("4200001569202208304701234567");

try {
  Transaction result = service.queryOrderById(queryRequest);
  System.out.println(result.getTradeState());
} catch (ServiceException e) {
  // API返回失败, 例如ORDER_NOT_EXISTS
  System.out.printf("code=[%s], message=[%s]\n", e.getErrorCode(), e.getErrorMessage());
  System.out.printf("reponse body=[%s]\n", e.getResponseBody());
}
```

### 关闭订单

```java
CloseOrderRequest closeRequest = new CloseOrderRequest();
closeRequest.setMchid("190000****");
closeRequest.setOutTradeNo("out_trade_no_001");
// 方法没有返回值，意味着成功时API返回204 No Content
service.closeOrder(closeRequest);
```

### 更多示例

为了方便开发者快速上手，微信支付给每个服务生成了示例代码 `XxxServiceExample.java`，可以在 [example](service/src/example) 中查看。例如：

+ [JsapiServiceExample.java](service/src/example/java/com/wechat/pay/java/service/payments/jsapi/JsapiServiceExample.java)
+ [FileServiceExample.java](service/src/example/java/com/wechat/pay/java/service/file/FileUploadServiceExample.java)

## 错误处理

SDK 使用的是 unchecked exception，会抛出四种自定义异常。每种异常发生的场景及推荐的处理方式如下：

- [HttpException](core/src/main/java/com/wechat/pay/java/core/exception/HttpException.java)：调用微信支付服务，当发生 HTTP 请求异常时抛出该异常。
    - 构建请求参数失败、发送请求失败、I/O错误：推荐上报监控和打印日志，并获取异常中的 HTTP 请求信息以定位问题。
- [ValidationException](core/src/main/java/com/wechat/pay/java/core/exception/ValidationException.java) ：当验证微信支付签名失败时抛出该异常。
  - 验证微信支付返回签名失败：上报监控和日志打印。
  - 验证微信支付回调通知签名失败：确认输入参数与 HTTP 请求信息是否一致，若一致，说明该回调通知参数被篡改导致验签失败。
- [ServiceException](core/src/main/java/com/wechat/pay/java/core/exception/ServiceException.java)：调用微信支付服务，发送 HTTP 请求成功，HTTP 状态码小于200或大于等于300。
    - 状态码为5xx：主动重试。
    - 状态码为其他：获取错误中的 `errorCode` 、`errorMessage`，上报监控和日志打印。
- [MalformedMessageException](core/src/main/java/com/wechat/pay/java/core/exception/MalformedMessageException.java)：服务返回成功，返回内容异常。
  - HTTP 返回` Content-Type` 不为 `application/json`：当前不支持其他类型的返回体，账单下载正在规划中。
  - 解析 HTTP 返回体失败：上报监控和日志打印。
  - 回调通知参数不正确：确认传入参数是否与 HTTP 请求信息一致，传入参数是否存在编码或者 HTML 转码问题。
  - 解析回调请求体为 JSON 字符串失败：上报监控和日志打印。
  - 解密回调通知内容失败：确认传入的 apiV3 密钥是否正确。

## 回调通知验签和解密

可以使用 [notification](core/src/main/java/com/wechat/pay/java/core/notification) 中的 `NotificationParser` 解析回调通知。具体步骤如下：

1. 获取HTTP请求头中的 `Wechatpay-Signature` 、 `Wechatpay-Nonce` 、 `Wechatpay-Timestamp` 、 `Wechatpay-Serial` 、 `Request-ID` 、`Wechatpay-Signature-Type` 对应的值，构建 `RequestParam` 。
2. 获取 HTTP 请求体的 `JSON` 纯文本。
3. 根据解密后的通知数据数据结构，构造解密对象类 `DecryptObject` 。支付结果通知解密对象类为 [`Transaction`](service/src/main/java/com/wechat/pay/java/service/payments/model/Transaction.java)，退款结果通知解密对象类为 [RefundNotification](service/src/main/java/com/wechat/pay/java/service/refund/model/RefundNotification.java)。
4. 使用微信支付平台证书（验签）和商户 APIv3 密钥（解密）初始化 `NotificationConfig` 和 `NotificationParser` 。
5. 使用请求参数 `requestParam` 和 `DecryptObject.class` ，调用 `parser.parse` 验签并解密报文。

```java
// 构造 RequestParam
RequestParam requestParam = new Builder()
        .serialNumber(wechatPayCertificateSerialNumber)
        .nonce(nonce)
        .signature(signature)
        .timestamp(timstamp)
// 若未设置signType，默认值为 WECHATPAY2-SHA256-RSA2048
        .signType(signType)
        .body(requestBody)
        .build();

// 初始化 NotificationConfig
NotificationConfig rsaNotificationConfig = new RSANotificationConfig.Builder()
        .apiV3Key(apiV3Key)
        .certificates(wechatPayCertificateString)
        .build();

// 初始化 NotificationParser
NotificationParser parser=new NotificationParser(rsaNotificationConfig);

// 验签并解密报文
DecryptObject decryptObject = parser.parse(requestParam,DecryptObject.class);
```

## 发送 HTTP 请求

如果 SDK 未支持你需要的接口，你可以使用 [OkHttpClientAdapter](core/src/main/java/com/wechat/pay/java/core/http/okhttp/OkHttpClientAdapter.java) 的实现类发送 HTTP 请求，它会自动生成签名和验证签名。

发送请求步骤如下：

1. 初始化 `OkHttpClientAdapter`。
2. 构建请求 `HttpRequest` 。
3. 调用 `httpClient.execute` 或者 `httpClient.get` 等方法来发送 HTTP 请求。`httpClient.execute` 支持发送 GET、PUT、POST、PATCH、DELETE 请求，也可以调用指定的 HTTP 方法发送请求。

[OkHttpClientAdapterTest](core/src/test/java/com/wechat/pay/java/core/http/OkHttpClientAdapterTest.java) 中演示了如何构造和发送 HTTP 请求。如果现有的 `OkHttpClientAdapter` 实现类不满足你的需求，可以继承 [AbstractHttpClient](core/src/main/java/com/wechat/pay/java/core/http/AbstractHttpClient.java) 拓展实现。

## 敏感信息加解密

为了保证通信过程中敏感信息字段（如用户的住址、银行卡号、手机号码等）的机密性，

+ 微信支付要求加密上送的敏感信息
+ 微信支付会加密下行的敏感信息

详见 [接口规则 - 敏感信息加解密](https://wechatpay-api.gitbook.io/wechatpay-api-v3/qian-ming-zhi-nan-1/min-gan-xin-xi-jia-mi)。

可以使用 [cipher](core/src/main/java/com/wechat/pay/java/core/cipher) 中的 `RSAPrivacyEncryptor` 和 `RSAPrivacyDecryptor` ，手动对敏感信息加解密。

```java
// 微信支付平台证书中的公钥
PublicKey wechatPayPublicKey = null;
String plaintext = "";
PrivacyEncryptor encryptor = new RSAPrivacyEncryptor(wechatPayPublicKey);
String ciphertext = encryptor.encryptToString(plaintext);
```

```java
// 商户私钥
PrivateKey merchantPrivateKey = null;
String ciphertext = "";
PrivacyDecryptor decryptor = new RSAPrivacyDecryptor(merchantPrivateKey);
String plaintext = decryptor.decryptToString(ciphertext);
```

[RSAPrivacyEncryptorTest](core/src/test/java/com/wechat/pay/java/core/cipher/RSAPrivacyEncryptorTest.java) 和 [RSAPrivacyDecryptorTest](core/src/test/java/com/wechat/pay/java/core/cipher/RSAPrivacyDecryptorTest.java) 中演示了如何使用以上函数做敏感信息加解密。

## 常见问题

### 如何下载平台证书？
使用[微信支付 APIv3 平台证书下载工具](https://github.com/wechatpay-apiv3/CertificateDownloader)。

### 为什么收到应答中的证书序列号和发起请求的证书序列号不一致？

请求和应答使用 [数字签名](https://zh.wikipedia.org/wiki/%E6%95%B8%E4%BD%8D%E7%B0%BD%E7%AB%A0) ，保证数据传递的真实、完整和不可否认。为了验签方能识别数字签名使用的密钥（特别是密钥和证书更换期间），微信支付 APIv3 要求签名和相应的证书序列号一起传输。

+ 商户请求使用**商户API私钥**签名。商户应上送商户证书序列号。
+ 微信支付应答使用**微信支付平台私钥**签名。微信支付应答返回微信支付平台证书序列号。

综上所述，请求和应答的证书序列号是不一致的。

### 证书和回调解密需要的 AesGcm 解密在哪里？

请参考 [AeadAesCipher](core/src/main/java/com/wechat/pay/java/core/cipher/AeadAesCipher.java) 和 [AeadAesCipherTest](core/src/test/java/com/wechat/pay/java/core/cipher/AeadAesCipherTest.java) 。

由于 SDK 已经提供了微信支付平台证书下载服务 `CertificateService` 以及回调通知解析器 `NotificationParser` ，这两者会完成所有的解析与解密工作。因此除非你想要自定义实现，否则你应该不需要用到 `AeadXxxCipher` 中提供的方法。

## 如何参与开发
微信支付欢迎来自社区的开发者贡献你们的想法和代码。请你在提交 PR 之前，先提一个对应的 issue 说明以下内容：

- 背景（如，遇到的问题）和目的。
- **着重**说明你的想法。
- 通过代码或者其他方式，简要的说明是如何实现的，或者它会是如何使用。
- 是否影响现有的接口。

## 联系微信支付

如果你发现了 BUG，或者需要的功能还未支持，或者有任何疑问、建议，欢迎通过 [issue](https://github.com/wechatpay-apiv3/wechatpay-java/issues) 反馈。

也欢迎访问微信支付的 [开发者社区](https://developers.weixin.qq.com/community/pay)。
