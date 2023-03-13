[![JavaDoc](http://img.shields.io/badge/javadoc-reference-blue.svg)](https://www.javadoc.io/doc/com.github.wechatpay-apiv3/wechatpay-java/latest/index.html)
![Maven Central](https://img.shields.io/maven-central/v/com.github.wechatpay-apiv3/wechatpay-java?versionPrefix=0.2.7)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=wechatpay-apiv3_wechatpay-java&metric=security_rating)](https://sonarcloud.io/summary/overall?id=wechatpay-apiv3_wechatpay-java)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=wechatpay-apiv3_wechatpay-java&metric=sqale_rating)](https://sonarcloud.io/summary/overall?id=wechatpay-apiv3_wechatpay-java)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=wechatpay-apiv3_wechatpay-java&metric=coverage)](https://sonarcloud.io/summary/overall?id=wechatpay-apiv3_wechatpay-java)

# 微信支付 APIv3 Java SDK

[微信支付 APIv3](https://wechatpay-api.gitbook.io/wechatpay-api-v3/) 官方 Java 语言客户端开发库。

开发库由 `core` 和 `service` 组成：

- core 为基础库，包含自动签名和验签的 HTTP 客户端、回调处理、加解密库。
- service 为业务服务，包含[业务接口](service/src/main/java/com/wechat/pay/java/service)和[使用示例](service/src/example/java/com/wechat/pay/java/service)。

## 帮助微信支付改进

为了向广大开发者提供更好的使用体验，微信支付诚挚邀请您反馈使用微信支付 Java SDK 中的感受。您的反馈将对改进 SDK 大有帮助，点击参与[问卷调查](https://wj.qq.com/s2/11503706/aa9a/)。

## 前置条件

- Java 1.8+。
- [成为微信支付商户](https://pay.weixin.qq.com/index.php/apply/applyment_home/guide_normal)。
- [商户 API 证书](https://wechatpay-api.gitbook.io/wechatpay-api-v3/ren-zheng/zheng-shu#shang-hu-api-zheng-shu)：指由商户申请的，包含商户的商户号、公司名称、公钥信息的证书。
- [商户 API 私钥](https://wechatpay-api.gitbook.io/wechatpay-api-v3/ren-zheng/zheng-shu#shang-hu-api-si-yao)：商户申请商户API证书时，会生成商户私钥，并保存在本地证书文件夹的文件 apiclient_key.pem 中。
- [APIv3 密钥](https://wechatpay-api.gitbook.io/wechatpay-api-v3/ren-zheng/api-v3-mi-yao)：为了保证安全性，微信支付在回调通知和平台证书下载接口中，对关键信息进行了 AES-256-GCM 加密。APIv3 密钥是加密时使用的对称密钥。

## 快速开始

### 安装

最新版本已经在 [Maven Central](https://search.maven.org/artifact/com.github.wechatpay-apiv3/wechatpay-java) 发布。

#### Gradle

在你的 build.gradle 文件中加入如下的依赖

```groovy
implementation 'com.github.wechatpay-apiv3:wechatpay-java:0.2.7'
```

#### Maven

加入以下依赖

```xml
<dependency>
  <groupId>com.github.wechatpay-apiv3</groupId>
  <artifactId>wechatpay-java</artifactId>
  <version>0.2.7</version>
</dependency>
```

### 调用业务请求接口

以 Native 支付下单为例，先补充商户号等必要参数以构建 `config`，再构建 `service` 即可调用 `prepay()` 发送请求。

```java
package com.wechat.pay.java.service;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;

/** Native 支付下单为例 */
public class QuickStart {

    /** 商户号 */
    public static String merchantId = "";
    /** 商户API私钥路径 */
    public static String privateKeyPath = "";
    /** 商户证书序列号 */
    public static String merchantSerialNumber = "";
    /** 商户APIV3密钥 */
    public static String apiV3key = "";

    public static void main(String[] args) {
        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(merchantId)
                        .privateKeyFromPath(privateKeyPath)
                        .merchantSerialNumber(merchantSerialNumber)
                        .apiV3Key(apiV3key)
                        .build();
        // 构建service
        NativePayService service = new NativePayService.Builder().config(config).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(100);
        request.setAmount(amount);
        request.setAppid("wxa9d9651ae******");
        request.setMchid("190000****");
        request.setDescription("测试商品标题");
        request.setNotifyUrl("https://notify_url");
        request.setOutTradeNo("out_trade_no_001");
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        System.out.println(response.getCodeUrl());
    }
}

```

从示例可见，使用 SDK 不需要计算请求签名和验证应答签名。详细代码可从 [QuickStart](service/src/example/java/com/wechat/pay/java/service/QuickStart.java) 获得。

## 示例

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

### 下单并生成调起支付的参数

JSAPI 支付和 APP 支付推荐使用服务拓展类 [JsapiServiceExtension](https://github.com/wechatpay-apiv3/wechatpay-java/blob/main/service/src/main/java/com/wechat/pay/java/service/payments/jsapi/JsapiServiceExtension.java) 和 [AppServiceExtension](https://github.com/wechatpay-apiv3/wechatpay-java/blob/main/service/src/main/java/com/wechat/pay/java/service/payments/app/AppServiceExtension.java)，两者包含了下单并返回调起支付参数方法。

```java
JsapiServiceExtension service = new JsapiServiceExtension.Builder().config(config).build();

// 跟之前下单示例一样，填充预下单参数
PrepayRequest request = new PrepayRequest();

// response包含了调起支付所需的所有参数，可直接用于前端调起支付
PrepayWithRequestPaymentResponse response = service.prepayWithRequestPayment(request);
```

### 更多示例

为了方便开发者快速上手，微信支付给每个服务生成了示例代码 `XxxServiceExample.java`，可以在 [example](service/src/example) 中查看。
例如：

- [JsapiServiceExtensionExample.java](service/src/example/java/com/wechat/pay/java/service/payments/jsapi/JsapiServiceExtensionExample.java)
- [FileServiceExample.java](service/src/example/java/com/wechat/pay/java/service/file/FileUploadServiceExample.java)

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
  - HTTP 返回 `Content-Type` 不为 `application/json`：不支持其他类型的返回体，[下载账单](#下载账单) 应使用 `download()` 方法。
  - 解析 HTTP 返回体失败：上报监控和日志打印。
  - 回调通知参数不正确：确认传入参数是否与 HTTP 请求信息一致，传入参数是否存在编码或者 HTML 转码问题。
  - 解析回调请求体为 JSON 字符串失败：上报监控和日志打印。
  - 解密回调通知内容失败：确认传入的 apiV3 密钥是否正确。

## 自动更新微信支付平台证书

在 API 请求过程中，客户端需使用微信支付平台证书，验证服务器应答的真实性和完整性。
在 v0.2.3 版本，我们加入了自动更新平台证书的配置类 `RSAAutoCertificateConfig`。

```java
Config config =
    new RSAAutoCertificateConfig.Builder()
        .merchantId(merchantId)
        .privateKeyFromPath(privateKeyPath)
        .merchantSerialNumber(merchantSerialNumber)
        .apiV3Key(apiV3key)
        .build();
```

`RSAAutoCertificateConfig` 通过 `RSAAutoCertificateProvider` 自动下载微信支付平台证书。
同时，`RSAAutoCertificateProvider` 会启动一个后台线程，定时更新证书（目前设计为60分钟），以实现证书过期时的新老证书平滑切换。

> **Note**
>
> 每个商户号只能创建一个 `RSAAutoCertificateConfig`。同一个商户号构造多个实例，会抛出 `IllegalStateException` 异常。
>
> 我们建议你将配置类作为全局变量。如果你的程序是多线程，建议使用**多线程安全**的单例模式。

### 使用本地的微信支付平台证书

如果你不想使用 SDK 提供的定时更新平台证书，你可以使用配置类 `RSAConfig` 加载本地证书。

```java
Config config =
    new RSAConfig.Builder()
        .merchantId(merchantId)
        .privateKeyFromPath(privateKeyPath)
        .merchantSerialNumber(merchantSerialNumber)
        .wechatPayCertificatesFromPath(wechatPayCertificatePath)
        .build();
```

## 回调通知验签和解密

首先，你需要在你的服务器上创建一个公开的 HTTP 端点，接受来自微信支付的回调通知。
当接收到回调通知，使用 [notification](core/src/main/java/com/wechat/pay/java/core/notification) 中的 `NotificationParser` 解析回调通知。

具体步骤如下：

1. 使用回调通知请求的数据，构建 `RequestParam`。
    - HTTP 头 `Wechatpay-Signature`
    - HTTP 头 `Wechatpay-Nonce`
    - HTTP 头 `Wechatpay-Timestamp`
    - HTTP 头 `Wechatpay-Serial`
    - HTTP 头 `Wechatpay-Signature-Type`
    - HTTP 请求体 body。切记使用原始报文，不要用 JSON 对象序列化后的字符串，避免验签的 body 和原文不一致。
2. 初始化 `RSAAutoCertificateConfig`。微信支付平台证书由 SDK 的自动更新平台能力提供，也可以使用本地证书。
3. 初始化 `NotificationParser`。
4. 调用 `NotificationParser.parse()` 验签、解密并将 JSON 转换成具体的通知回调对象。

```java
// 构造 RequestParam
RequestParam requestParam = new RequestParam.Builder()
        .serialNumber(wechatPayCertificateSerialNumber)
        .nonce(nonce)
        .signature(signature)
        .timestamp(timestamp)
        .body(requestBody)
        .build();

// 如果已经初始化了 RSAAutoCertificateConfig，可直接使用
// 没有的话，则构造一个
NotificationConfig config = new RSAAutoCertificateConfig.Builder()
        .merchantId(merchantId)
        .privateKeyFromPath(privateKeyPath)
        .merchantSerialNumber(merchantSerialNumber)
        .apiV3Key(apiV3key)
        .build();

// 初始化 NotificationParser
NotificationParser parser = new NotificationParser(config);

// 以支付通知回调为例，验签、解密并转换成 Transaction
Transaction transaction = parser.parse(requestParam, Transaction.class);
```

常用的通知回调对象类型：

+ 支付 `Transaction`
+ 退款 `RefundNotification`
+ 若 SDK 暂不支持的类型，请使用 `Map.class`，嵌套的 Json 对象将被转换成 `LinkedTreeMap`

你既可以为每个通知回调使用不同的 HTTP 端点，也可以使用一个端点根据 `event_type` 处理不同的通知回调。
我们建议，不同的通知回调使用不同的端点，直接调用 SDK 处理通知回调，避免商户自己解析报文。因为 SDK 会先验证通知回调的有效性，可有效防止"坏人"的报文攻击。

## 发送 HTTP 请求

如果 SDK 未支持你需要的接口，你可以使用 [OkHttpClientAdapter](core/src/main/java/com/wechat/pay/java/core/http/okhttp/OkHttpClientAdapter.java) 的实现类发送 HTTP 请求，它会自动生成签名和验证签名。

发送请求步骤如下：

1. 初始化 `OkHttpClientAdapter`，建议使用 `DefaultHttpClientBuilder` 构建。
2. 构建请求 `HttpRequest`。
3. 调用 `httpClient.execute` 或者 `httpClient.get` 等方法来发送 HTTP 请求。`httpClient.execute` 支持发送 GET、PUT、POST、PATCH、DELETE 请求，也可以调用指定的 HTTP 方法发送请求。

[OkHttpClientAdapterTest](core/src/test/java/com/wechat/pay/java/core/http/OkHttpClientAdapterTest.java) 中演示了如何构造和发送 HTTP 请求。如果现有的 `OkHttpClientAdapter` 实现类不满足你的需求，可以继承 [AbstractHttpClient](core/src/main/java/com/wechat/pay/java/core/http/AbstractHttpClient.java) 拓展实现。

### 下载账单

因为下载的账单文件可能会很大，为了平衡系统性能和签名验签的实现成本，[账单下载API](https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_8.shtml) 被分成了两个步骤：

1. `/v3/bill/tradebill` 申请账单下载链接，并获取账单摘要。
1. `/v3/billdownload/file` 账单文件下载，请求需签名但应答不签名。

SDK 提供了 `HttpClient.download()` 方法。它返回账单的输入流。开发者使用完输入流后，应自主关闭流。

```java
InputStream inputStream = httpClient.download(downloadUrl);

// 非压缩的账单可使用 core.util.IOUtil 从流读入内存字符串，大账单请慎用
String respBody = IOUtil.toString(inputStream);
inputStream.close();
```

> **Warning**
>
> 开发者在下载文件之后，应使用第一步获取的账单摘要校验文件的完整性。

## 敏感信息加解密

为了保证通信过程中敏感信息字段（如用户的住址、银行卡号、手机号码等）的机密性，

- 微信支付要求加密上送的敏感信息
- 微信支付会加密下行的敏感信息

详见 [接口规则 - 敏感信息加解密](https://wechatpay-api.gitbook.io/wechatpay-api-v3/qian-ming-zhi-nan-1/min-gan-xin-xi-jia-mi)。

### 自动加解密

如果是 SDK 已支持的接口，例如商家转账，SDK 将根据契约自动对敏感信息做加解密：

- 发起请求时，开发者设置原文。SDK 自动加密敏感信息，并设置 `Wechatpay-Serial` 请求头
- 收到应答时，解密器自动解密敏感信息，开发者得到原文

### 手动加解密

如果是 SDK 尚未支持的接口，你可以使用 [cipher](core/src/main/java/com/wechat/pay/java/core/cipher) 中的 `RSAPrivacyEncryptor` 和 `RSAPrivacyDecryptor` ，手动对敏感信息加解密。

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

## 日志

SDK 使用了 [SLF4j](http://www.slf4j.org/) 作为日志框架的接口。这样，你可以使用你熟悉的日志框架，例如 [Logback](https://logback.qos.ch/documentation.html)、[Log4j2](https://github.com/apache/logging-log4j2) 或者 [SLF4j-simple](https://www.slf4j.org/manual.html)。
SDK 的日志会跟你的日志记录在一起。

为了启用日志，你应在你的构建脚本中添加日志框架的依赖。如果不配置日志框架，默认是使用 SLF4j 提供的 空（NOP）日志实现，它不会记录任何日志。

## 使用国密

我们提供基于 [腾讯 Kona 国密套件](https://github.com/Tencent/TencentKonaSMSuite) 的国密扩展。文档请参考 [shangmi/README.md](shangmi/README.md)。

## 常见问题

### 为什么收到应答中的证书序列号和发起请求的证书序列号不一致？

请求和应答使用 [数字签名](https://zh.wikipedia.org/wiki/%E6%95%B8%E4%BD%8D%E7%B0%BD%E7%AB%A0) ，保证数据传递的真实、完整和不可否认。为了验签方能识别数字签名使用的密钥（特别是密钥和证书更换期间），微信支付 APIv3 要求签名和相应的证书序列号一起传输。

- 商户请求使用**商户API私钥**签名。商户应上送商户证书序列号。
- 微信支付应答使用**微信支付平台私钥**签名。微信支付应答返回微信支付平台证书序列号。

综上所述，请求和应答的证书序列号是不一致的。

### 证书和回调解密需要的 AesGcm 解密在哪里？

请参考 [AeadAesCipher](core/src/main/java/com/wechat/pay/java/core/cipher/AeadAesCipher.java) 和 [AeadAesCipherTest](core/src/test/java/com/wechat/pay/java/core/cipher/AeadAesCipherTest.java) 。

由于 SDK 已经提供了微信支付平台证书下载服务 `CertificateService` 以及回调通知解析器 `NotificationParser` ，这两者会完成所有的解析与解密工作。因此除非你想要自定义实现，否则你应该不需要用到 `AeadXxxCipher` 中提供的方法。

### 为什么我使用 `NotificationHandler` 验证回调通知失败，抛出 `ValidationException`？

如果你使用的是 SDK 自动更新的微信支付平台证书，验证失败原因是：参与验证的参数不正确。从开发者反馈来看，大部分失败案例没有使用回调原始 body，而是用 body 反序列化得到的对象再做 JSON 序列化得到的 body。很遗憾，这样的 body 几乎一定跟原始报文**不一致**，所以签名验证不通过。具体案例可参考 [#112](https://github.com/wechatpay-apiv3/wechatpay-java/issues/112)。

如果你使用的是本地的微信支付平台证书，请检查微信支付平台证书是否正确，不要把商户证书和微信支付平台证书搞混了。

### 如何计算前端签名？

有一部分 API 需要计算前端签名，例如调起支付、调起支付分小程序等。

- 调起支付签名，SDK 提供了下单并生成调起支付参数的方法，请参考 [示例](#下单并生成调起支付的参数)。

- 其他场景计算签名，请参考 [JsapiServiceExtension](https://github.com/wechatpay-apiv3/wechatpay-java/blob/968a2ff8fb35c808f82827342abb100e30691a98/service/src/main/java/com/wechat/pay/java/service/payments/jsapi/JsapiServiceExtension.java#L59) 使用 [Signer](https://github.com/wechatpay-apiv3/wechatpay-java/blob/main/core/src/main/java/com/wechat/pay/java/core/cipher/Signer.java) 计算签名的例子。

### 为什么快速开始的示例程序执行后，程序不会退出？

是的，因为示例使用了自动更新微信支付平台证书，它会启动一个背景线程以定时更新证书。这个线程不会自动退出。

你可以主动终止程序，退出不会有副作用。 我们也在考虑如何提供优雅的退出方式。

## 如何参与开发

微信支付欢迎来自社区的开发者贡献你们的想法和代码。请你在提交 PR 之前，先提一个对应的 issue 说明以下内容：

- 背景（如，遇到的问题）和目的。
- **着重**说明你的想法。
- 通过代码或者其他方式，简要的说明是如何实现的，或者它会是如何使用。
- 是否影响现有的接口。

## 联系微信支付

如果你发现了 BUG，或者需要的功能还未支持，或者有任何疑问、建议，欢迎通过 [issue](https://github.com/wechatpay-apiv3/wechatpay-java/issues) 反馈。

也欢迎访问微信支付的 [开发者社区](https://developers.weixin.qq.com/community/pay)。
