package com.wechat.pay.java.service.payscore.permission;

import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.payscore.permission.model.ApplyPermissionsRequest;
import com.wechat.pay.java.service.payscore.permission.model.ApplyPermissionsResponse;
import com.wechat.pay.java.service.payscore.permission.model.GetPermissionsByAuthorizationCodeRequest;
import com.wechat.pay.java.service.payscore.permission.model.GetPermissionsByOpenIdRequest;
import com.wechat.pay.java.service.payscore.permission.model.OrderSceneApplyPermissionsRequest;
import com.wechat.pay.java.service.payscore.permission.model.OrderSceneApplyPermissionsResponse;
import com.wechat.pay.java.service.payscore.permission.model.PermissionsEntity;
import com.wechat.pay.java.service.payscore.permission.model.SceneApplyPermissionsRequest;
import com.wechat.pay.java.service.payscore.permission.model.SceneApplyPermissionsResponse;
import com.wechat.pay.java.service.payscore.permission.model.TerminatePermissionsByCodeRequest;
import com.wechat.pay.java.service.payscore.permission.model.TerminatePermissionsByOpenIdRequest;
import com.wechat.pay.java.service.payscore.permission.model.UpgradeUserPermissionsRequest;

/** PermissionService使用示例 */
public class PermissionServiceExample {

  public static String merchantId = "";
  public static String privateKeyPath = "";
  public static String merchantSerialNumber = "";
  public static String wechatPayCertificatePath = "";
  public static PermissionService service;

  public static void main(String[] args) {
    // 初始化商户配置
    RSAConfig config =
        new RSAConfig.Builder()
            .merchantId(merchantId)
            // 使用 com.wechat.pay.java.core.util
            // 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .wechatPayCertificatesFromPath(wechatPayCertificatePath)
            .build();

    // 初始化服务
    service = new PermissionService(config);
    // ... 调用接口
  }
  /** 商户预授权 */
  public static ApplyPermissionsResponse applyPermissions() {
    ApplyPermissionsRequest request = new ApplyPermissionsRequest();
    return service.applyPermissions(request);
  }
  /** 商户查询与用户的授权记录 */
  public static PermissionsEntity getPermissionsByAuthorizationCode() {

    GetPermissionsByAuthorizationCodeRequest request =
        new GetPermissionsByAuthorizationCodeRequest();
    return service.getPermissionsByAuthorizationCode(request);
  }
  /** 商户查询与用户授权记录 */
  public static PermissionsEntity getPermissionsByOpenId() {

    GetPermissionsByOpenIdRequest request = new GetPermissionsByOpenIdRequest();
    return service.getPermissionsByOpenId(request);
  }
  /** 商户交易场景预授权 */
  public static OrderSceneApplyPermissionsResponse orderSceneApplyPermissions() {
    OrderSceneApplyPermissionsRequest request = new OrderSceneApplyPermissionsRequest();
    return service.orderSceneApplyPermissions(request);
  }
  /** 商户场景中预授权 */
  public static SceneApplyPermissionsResponse sceneApplyPermissions() {
    SceneApplyPermissionsRequest request = new SceneApplyPermissionsRequest();
    return service.sceneApplyPermissions(request);
  }
  /** 商户解除用户授权关系 */
  public static void terminatePermissionsByCode() {

    TerminatePermissionsByCodeRequest request = new TerminatePermissionsByCodeRequest();
    service.terminatePermissionsByCode(request);
  }
  /** 商户解除用户授权关系 */
  public static void terminatePermissionsByOpenId() {

    TerminatePermissionsByOpenIdRequest request = new TerminatePermissionsByOpenIdRequest();
    service.terminatePermissionsByOpenId(request);
  }
  /** 商户升级用户服务授权 */
  public static PermissionsEntity upgradeUserPermissions() {

    UpgradeUserPermissionsRequest request = new UpgradeUserPermissionsRequest();
    return service.upgradeUserPermissions(request);
  }
}
