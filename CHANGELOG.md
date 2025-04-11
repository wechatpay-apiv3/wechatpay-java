# ChangeLog

## [0.2.17] - 2025-04-11

### Modified

+ 修复 Apache Http Client 未能正确设置 Charset 为 UTF-8 的问题
+ 修复 ApacheHttpResponse 未能正确关闭的问题

## [0.2.16] - 2025-02-10

### Added

+ 支持 Apache Http Client


## [0.2.15] - 2024-11-20

### Added

+ 自动填充平台证书序列号或公钥ID到请求的 `Wechatpay-Serial` 头

## [0.2.14] - 2024-08-25

### Added

+ 支持使用微信支付公钥验证微信支付应答和回调签名

## 【0.2.13】 - 2024-06-17

### Added

+ 支持商家转账到零钱的 `notify_url`
+ 兼容 JDK17LTS
