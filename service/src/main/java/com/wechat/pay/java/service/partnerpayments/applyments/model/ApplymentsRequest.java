package com.wechat.pay.java.service.partnerpayments.applyments.model;

import com.google.gson.annotations.SerializedName;

public class ApplymentsRequest {
  /** 业务申请编号 */
  @SerializedName("out_request_no")
  private String outRequestNo;

  /** 主体类型 */
  @SerializedName("organization_type")
  private String organizationType;

  /** 是否金融机构 */
  @SerializedName("finance_institution")
  private Boolean financeInstitution;

  /** 营业执照信息 */
  @SerializedName("business_license_info")
  private BusinessLicenseInfo businessLicenseInfo;

  /** 金融机构许可证信息 */
  @SerializedName("finance_institution_info")
  private FinanceInstitutionInfo financeInstitutionInfo;

  /** 证件持有人类型 */
  @SerializedName("id_holder_type")
  private String idHolderType;

  /** 经营者/法人证件类型 */
  @SerializedName("id_doc_type")
  private String idDocType;

  /** 法定代表人说明函 */
  @SerializedName("authorize_letter_copy")
  private String authorizeLetterCopy;

  /** 经营者/法人身份证信息 */
  @SerializedName("id_card_info")
  private IdCardInfo idCardInfo;

  /** 经营者/法人其他类型证件信息 */
  @SerializedName("id_doc_info")
  private IdDocInfo idDocInfo;

  /** 经营者/法人是否为受益人 */
  @SerializedName("owner")
  private Boolean owner;

  /** 最终受益人信息列表 */
  @SerializedName("ubo_info_list")
  private UboInfo[] uboInfoList;

  /** 结算账户信息 */
  @SerializedName("account_info")
  private AccountInfo accountInfo;

  /** 超级管理员信息 */
  @SerializedName("contact_info")
  private ContactInfo contactInfo;

  /** 经营场景信息 */
  @SerializedName("sales_scene_info")
  private SalesSceneInfo salesSceneInfo;

  /** 结算规则 */
  @SerializedName("settlement_info")
  private SettlementInfo settlementInfo;

  /** 商户简称 */
  @SerializedName("merchant_shortname")
  private String merchantShortname;

  /** 特殊资质 */
  @SerializedName("qualifications")
  private String qualifications;

  /** 补充材料 */
  @SerializedName("business_addition_pics")
  private String businessAdditionPics;

  /** 补充说明 */
  @SerializedName("business_addition_desc")
  private String businessAdditionDesc;

  public static class BusinessLicenseInfo {
    /** 证书类型 */
    @SerializedName("cert_type")
    private String certType;

    /** 营业执照扫描件 */
    @SerializedName("business_license_copy")
    private String businessLicenseCopy;

    /** 营业执照注册号 */
    @SerializedName("business_license_number")
    private String businessLicenseNumber;

    /** 商户名称 */
    @SerializedName("merchant_name")
    private String merchantName;

    /** 经营者/法定代表人姓名 */
    @SerializedName("legal_person")
    private String legalPerson;

    /** 注册地址 */
    @SerializedName("company_address")
    private String companyAddress;

    /** 营业期限 */
    @SerializedName("business_time")
    private String[] businessTime;

    public String getCertType() {
      return certType;
    }

    public void setCertType(String certType) {
      this.certType = certType;
    }

    public String getBusinessLicenseCopy() {
      return businessLicenseCopy;
    }

    public void setBusinessLicenseCopy(String businessLicenseCopy) {
      this.businessLicenseCopy = businessLicenseCopy;
    }

    public String getBusinessLicenseNumber() {
      return businessLicenseNumber;
    }

    public void setBusinessLicenseNumber(String businessLicenseNumber) {
      this.businessLicenseNumber = businessLicenseNumber;
    }

    public String getMerchantName() {
      return merchantName;
    }

    public void setMerchantName(String merchantName) {
      this.merchantName = merchantName;
    }

    public String getLegalPerson() {
      return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
      this.legalPerson = legalPerson;
    }

    public String getCompanyAddress() {
      return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
      this.companyAddress = companyAddress;
    }

    public String[] getBusinessTime() {
      return businessTime;
    }

    public void setBusinessTime(String[] businessTime) {
      this.businessTime = businessTime;
    }
  }

  public static class FinanceInstitutionInfo {
    /** 金融机构类型 */
    @SerializedName("finance_type")
    private String financeType;

    /** 金融机构许可证图片 */
    @SerializedName("finance_license_pics")
    private String[] financeLicensePics;
  }

  public static class IdCardInfo {
    /** 身份证人像面照片 */
    @SerializedName("id_card_copy")
    private String idCardCopy;

    /** 身份证国徽面照片 */
    @SerializedName("id_card_national")
    private String idCardNational;

    /** 身份证姓名 */
    @SerializedName("id_card_name")
    private String idCardName;

    /** 身份证号码 */
    @SerializedName("id_card_number")
    private String idCardNumber;

    /** 身份证开始时间 */
    @SerializedName("id_card_valid_time_begin")
    private String idCardValidTimeBegin;

    /** 身份证结束时间 */
    @SerializedName("id_card_valid_time")
    private String idCardValidTime;

    public String getIdCardCopy() {
      return idCardCopy;
    }

    public void setIdCardCopy(String idCardCopy) {
      this.idCardCopy = idCardCopy;
    }

    public String getIdCardNational() {
      return idCardNational;
    }

    public void setIdCardNational(String idCardNational) {
      this.idCardNational = idCardNational;
    }

    public String getIdCardName() {
      return idCardName;
    }

    public void setIdCardName(String idCardName) {
      this.idCardName = idCardName;
    }

    public String getIdCardNumber() {
      return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
      this.idCardNumber = idCardNumber;
    }

    public String getIdCardValidTimeBegin() {
      return idCardValidTimeBegin;
    }

    public void setIdCardValidTimeBegin(String idCardValidTimeBegin) {
      this.idCardValidTimeBegin = idCardValidTimeBegin;
    }

    public String getIdCardValidTime() {
      return idCardValidTime;
    }

    public void setIdCardValidTime(String idCardValidTime) {
      this.idCardValidTime = idCardValidTime;
    }
  }

  public static class IdDocInfo {
    /** 证件姓名 */
    @SerializedName("id_doc_name")
    private String idDocName;

    /** 证件号码 */
    @SerializedName("id_doc_number")
    private String idDocNumber;

    /** 证件正面照片 */
    @SerializedName("id_doc_copy")
    private String idDocCopy;

    /** 证件反面照片 */
    @SerializedName("id_doc_copy_back")
    private String idDocCopyBack;

    /** 证件开始日期 */
    @SerializedName("doc_period_begin")
    private String docPeriodBegin;

    /** 证件结束日期 */
    @SerializedName("doc_period_end")
    private String docPeriodEnd;
  }

  public static class UboInfo {
    /** 证件类型 */
    @SerializedName("ubo_id_doc_type")
    private String uboIdDocType;

    /** 证件人像面照片 */
    @SerializedName("ubo_id_doc_copy")
    private String uboIdDocCopy;

    /** 证件国徽面照片 */
    @SerializedName("ubo_id_doc_copy_back")
    private String uboIdDocCopyBack;

    /** 证件姓名 */
    @SerializedName("ubo_id_doc_name")
    private String uboIdDocName;

    /** 证件号码 */
    @SerializedName("ubo_id_doc_number")
    private String uboIdDocNumber;

    /** 证件地址 */
    @SerializedName("ubo_id_doc_address")
    private String uboIdDocAddress;

    /** 证件开始日期 */
    @SerializedName("ubo_id_doc_period_begin")
    private String uboIdDocPeriodBegin;

    /** 证件结束日期 */
    @SerializedName("ubo_id_doc_period_end")
    private String uboIdDocPeriodEnd;

    public String getUboIdDocType() {
      return uboIdDocType;
    }

    public void setUboIdDocType(String uboIdDocType) {
      this.uboIdDocType = uboIdDocType;
    }

    public String getUboIdDocCopy() {
      return uboIdDocCopy;
    }

    public void setUboIdDocCopy(String uboIdDocCopy) {
      this.uboIdDocCopy = uboIdDocCopy;
    }

    public String getUboIdDocCopyBack() {
      return uboIdDocCopyBack;
    }

    public void setUboIdDocCopyBack(String uboIdDocCopyBack) {
      this.uboIdDocCopyBack = uboIdDocCopyBack;
    }

    public String getUboIdDocName() {
      return uboIdDocName;
    }

    public void setUboIdDocName(String uboIdDocName) {
      this.uboIdDocName = uboIdDocName;
    }

    public String getUboIdDocNumber() {
      return uboIdDocNumber;
    }

    public void setUboIdDocNumber(String uboIdDocNumber) {
      this.uboIdDocNumber = uboIdDocNumber;
    }

    public String getUboIdDocAddress() {
      return uboIdDocAddress;
    }

    public void setUboIdDocAddress(String uboIdDocAddress) {
      this.uboIdDocAddress = uboIdDocAddress;
    }

    public String getUboIdDocPeriodBegin() {
      return uboIdDocPeriodBegin;
    }

    public void setUboIdDocPeriodBegin(String uboIdDocPeriodBegin) {
      this.uboIdDocPeriodBegin = uboIdDocPeriodBegin;
    }

    public String getUboIdDocPeriodEnd() {
      return uboIdDocPeriodEnd;
    }

    public void setUboIdDocPeriodEnd(String uboIdDocPeriodEnd) {
      this.uboIdDocPeriodEnd = uboIdDocPeriodEnd;
    }
  }

  public static class AccountInfo {
    /** 账户类型 */
    @SerializedName("bank_account_type")
    private String bankAccountType;

    /** 开户银行 */
    @SerializedName("account_bank")
    private String accountBank;

    /** 开户名称 */
    @SerializedName("account_name")
    private String accountName;

    /** 开户银行省市编码 */
    @SerializedName("bank_address_code")
    private String bankAddressCode;

    /** 开户银行联行号 */
    @SerializedName("bank_branch_id")
    private String bankBranchId;

    /** 开户银行全称（含支行） */
    @SerializedName("bank_name")
    private String bankName;

    /** 银行账号 */
    @SerializedName("account_number")
    private String accountNumber;

    public String getBankAccountType() {
      return bankAccountType;
    }

    public void setBankAccountType(String bankAccountType) {
      this.bankAccountType = bankAccountType;
    }

    public String getAccountBank() {
      return accountBank;
    }

    public void setAccountBank(String accountBank) {
      this.accountBank = accountBank;
    }

    public String getAccountName() {
      return accountName;
    }

    public void setAccountName(String accountName) {
      this.accountName = accountName;
    }

    public String getBankAddressCode() {
      return bankAddressCode;
    }

    public void setBankAddressCode(String bankAddressCode) {
      this.bankAddressCode = bankAddressCode;
    }

    public String getBankBranchId() {
      return bankBranchId;
    }

    public void setBankBranchId(String bankBranchId) {
      this.bankBranchId = bankBranchId;
    }

    public String getBankName() {
      return bankName;
    }

    public void setBankName(String bankName) {
      this.bankName = bankName;
    }

    public String getAccountNumber() {
      return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
      this.accountNumber = accountNumber;
    }
  }

  public static class ContactInfo {
    /** 超级管理员类型 */
    @SerializedName("contact_type")
    private String contactType;

    /** 超级管理员姓名 */
    @SerializedName("contact_name")
    private String contactName;

    /** 超级管理员证件类型 */
    @SerializedName("contact_id_doc_type")
    private String contactIdDocType;

    /** 超级管理员证件号码 */
    @SerializedName("contact_id_card_number")
    private String contactIdCardNumber;

    /** 超级管理员证件正面照片 */
    @SerializedName("contact_id_doc_copy")
    private String contactIdDocCopy;

    /** 超级管理员证件反面照片 */
    @SerializedName("contact_id_doc_copy_back")
    private String contactIdDocCopyBack;

    /** 超级管理员证件有效期开始时间 */
    @SerializedName("contact_id_doc_period_begin")
    private String contactIdDocPeriodBegin;

    /** 超级管理员证件有效期结束时间 */
    @SerializedName("contact_id_doc_period_end")
    private String contactIdDocPeriodEnd;

    /** 业务办理授权函 */
    @SerializedName("business_authorization_letter")
    private String businessAuthorizationLetter;

    /** 超级管理员手机 */
    @SerializedName("mobile_phone")
    private String mobilePhone;

    /** 超级管理员邮箱 */
    @SerializedName("contact_email")
    private String contactEmail;

    public String getContactType() {
      return contactType;
    }

    public void setContactType(String contactType) {
      this.contactType = contactType;
    }

    public String getContactName() {
      return contactName;
    }

    public void setContactName(String contactName) {
      this.contactName = contactName;
    }

    public String getContactIdDocType() {
      return contactIdDocType;
    }

    public void setContactIdDocType(String contactIdDocType) {
      this.contactIdDocType = contactIdDocType;
    }

    public String getContactIdCardNumber() {
      return contactIdCardNumber;
    }

    public void setContactIdCardNumber(String contactIdCardNumber) {
      this.contactIdCardNumber = contactIdCardNumber;
    }

    public String getContactIdDocCopy() {
      return contactIdDocCopy;
    }

    public void setContactIdDocCopy(String contactIdDocCopy) {
      this.contactIdDocCopy = contactIdDocCopy;
    }

    public String getContactIdDocCopyBack() {
      return contactIdDocCopyBack;
    }

    public void setContactIdDocCopyBack(String contactIdDocCopyBack) {
      this.contactIdDocCopyBack = contactIdDocCopyBack;
    }

    public String getContactIdDocPeriodBegin() {
      return contactIdDocPeriodBegin;
    }

    public void setContactIdDocPeriodBegin(String contactIdDocPeriodBegin) {
      this.contactIdDocPeriodBegin = contactIdDocPeriodBegin;
    }

    public String getContactIdDocPeriodEnd() {
      return contactIdDocPeriodEnd;
    }

    public void setContactIdDocPeriodEnd(String contactIdDocPeriodEnd) {
      this.contactIdDocPeriodEnd = contactIdDocPeriodEnd;
    }

    public String getBusinessAuthorizationLetter() {
      return businessAuthorizationLetter;
    }

    public void setBusinessAuthorizationLetter(String businessAuthorizationLetter) {
      this.businessAuthorizationLetter = businessAuthorizationLetter;
    }

    public String getMobilePhone() {
      return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
      this.mobilePhone = mobilePhone;
    }

    public String getContactEmail() {
      return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
      this.contactEmail = contactEmail;
    }
  }

  public static class SalesSceneInfo {
    /** 店铺名称 */
    @SerializedName("store_name")
    private String storeName;

    /** 店铺链接 */
    @SerializedName("store_url")
    private String storeUrl;

    /** 店铺二维码 */
    @SerializedName("store_qr_code")
    private String storeQrCode;

    /** 商家小程序APPID */
    @SerializedName("mini_program_sub_appid")
    private String miniProgramSubAppid;

    public String getStoreName() {
      return storeName;
    }

    public void setStoreName(String storeName) {
      this.storeName = storeName;
    }

    public String getStoreUrl() {
      return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
      this.storeUrl = storeUrl;
    }

    public String getStoreQrCode() {
      return storeQrCode;
    }

    public void setStoreQrCode(String storeQrCode) {
      this.storeQrCode = storeQrCode;
    }

    public String getMiniProgramSubAppid() {
      return miniProgramSubAppid;
    }

    public void setMiniProgramSubAppid(String miniProgramSubAppid) {
      this.miniProgramSubAppid = miniProgramSubAppid;
    }
  }

  public static class SettlementInfo {
    /** 结算规则ID */
    @SerializedName("settlement_id")
    private Integer settlementId;

    /** 所属行业 */
    @SerializedName("qualification_type")
    private String qualificationType;
  }

  // Getters and Setters
  public String getOutRequestNo() {
    return outRequestNo;
  }

  public void setOutRequestNo(String outRequestNo) {
    this.outRequestNo = outRequestNo;
  }

  public String getOrganizationType() {
    return organizationType;
  }

  public void setOrganizationType(String organizationType) {
    this.organizationType = organizationType;
  }

  public Boolean getFinanceInstitution() {
    return financeInstitution;
  }

  public void setFinanceInstitution(Boolean financeInstitution) {
    this.financeInstitution = financeInstitution;
  }

  public BusinessLicenseInfo getBusinessLicenseInfo() {
    return businessLicenseInfo;
  }

  public void setBusinessLicenseInfo(BusinessLicenseInfo businessLicenseInfo) {
    this.businessLicenseInfo = businessLicenseInfo;
  }

  public FinanceInstitutionInfo getFinanceInstitutionInfo() {
    return financeInstitutionInfo;
  }

  public void setFinanceInstitutionInfo(FinanceInstitutionInfo financeInstitutionInfo) {
    this.financeInstitutionInfo = financeInstitutionInfo;
  }

  public String getIdHolderType() {
    return idHolderType;
  }

  public void setIdHolderType(String idHolderType) {
    this.idHolderType = idHolderType;
  }

  public String getIdDocType() {
    return idDocType;
  }

  public void setIdDocType(String idDocType) {
    this.idDocType = idDocType;
  }

  public String getAuthorizeLetterCopy() {
    return authorizeLetterCopy;
  }

  public void setAuthorizeLetterCopy(String authorizeLetterCopy) {
    this.authorizeLetterCopy = authorizeLetterCopy;
  }

  public IdCardInfo getIdCardInfo() {
    return idCardInfo;
  }

  public void setIdCardInfo(IdCardInfo idCardInfo) {
    this.idCardInfo = idCardInfo;
  }

  public IdDocInfo getIdDocInfo() {
    return idDocInfo;
  }

  public void setIdDocInfo(IdDocInfo idDocInfo) {
    this.idDocInfo = idDocInfo;
  }

  public Boolean getOwner() {
    return owner;
  }

  public void setOwner(Boolean owner) {
    this.owner = owner;
  }

  public UboInfo[] getUboInfoList() {
    return uboInfoList;
  }

  public void setUboInfoList(UboInfo[] uboInfoList) {
    this.uboInfoList = uboInfoList;
  }

  public AccountInfo getAccountInfo() {
    return accountInfo;
  }

  public void setAccountInfo(AccountInfo accountInfo) {
    this.accountInfo = accountInfo;
  }

  public ContactInfo getContactInfo() {
    return contactInfo;
  }

  public void setContactInfo(ContactInfo contactInfo) {
    this.contactInfo = contactInfo;
  }

  public SalesSceneInfo getSalesSceneInfo() {
    return salesSceneInfo;
  }

  public void setSalesSceneInfo(SalesSceneInfo salesSceneInfo) {
    this.salesSceneInfo = salesSceneInfo;
  }

  public SettlementInfo getSettlementInfo() {
    return settlementInfo;
  }

  public void setSettlementInfo(SettlementInfo settlementInfo) {
    this.settlementInfo = settlementInfo;
  }

  public String getMerchantShortname() {
    return merchantShortname;
  }

  public void setMerchantShortname(String merchantShortname) {
    this.merchantShortname = merchantShortname;
  }

  public String getQualifications() {
    return qualifications;
  }

  public void setQualifications(String qualifications) {
    this.qualifications = qualifications;
  }

  public String getBusinessAdditionPics() {
    return businessAdditionPics;
  }

  public void setBusinessAdditionPics(String businessAdditionPics) {
    this.businessAdditionPics = businessAdditionPics;
  }

  public String getBusinessAdditionDesc() {
    return businessAdditionDesc;
  }

  public void setBusinessAdditionDesc(String businessAdditionDesc) {
    this.businessAdditionDesc = businessAdditionDesc;
  }
}
