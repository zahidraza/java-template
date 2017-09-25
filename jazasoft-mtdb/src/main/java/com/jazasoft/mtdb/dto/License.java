package com.jazasoft.mtdb.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mdzahidraza on 23/09/17.
 */
public class License implements Serializable{

    private String status;

    private String message;

    private String productCode;

    private String productKey;

    private Date purchasedOn;

    private Date activatedOn;

    private boolean activated;

    private boolean expired;

    private String macId;

    private String licenseType;

    private String licenseFlavour;

    private Integer validity;   // -1: perpetual, n: Annual|Trial   [in number of days]

    private Long entitlement;  // Perpetual: Not applicable, Trial|Annual: No of active Users

    private String entitlementType;

    public License() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public Date getPurchasedOn() {
        return purchasedOn;
    }

    public void setPurchasedOn(Date purchasedOn) {
        this.purchasedOn = purchasedOn;
    }

    public Date getActivatedOn() {
        return activatedOn;
    }

    public void setActivatedOn(Date activatedOn) {
        this.activatedOn = activatedOn;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getLicenseFlavour() {
        return licenseFlavour;
    }

    public void setLicenseFlavour(String licenseFlavour) {
        this.licenseFlavour = licenseFlavour;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    public Long getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(Long entitlement) {
        this.entitlement = entitlement;
    }

    public String getEntitlementType() {
        return entitlementType;
    }

    public void setEntitlementType(String entitlementType) {
        this.entitlementType = entitlementType;
    }

    @Override
    public String toString() {
        return "License{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productKey='" + productKey + '\'' +
                ", purchasedOn=" + purchasedOn +
                ", activatedOn=" + activatedOn +
                ", activated=" + activated +
                ", expired=" + expired +
                ", macId='" + macId + '\'' +
                ", licenseType='" + licenseType + '\'' +
                ", licenseFlavour='" + licenseFlavour + '\'' +
                ", validity=" + validity +
                ", entitlement=" + entitlement +
                ", entitlementType='" + entitlementType + '\'' +
                '}';
    }
}
