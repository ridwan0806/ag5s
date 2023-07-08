package com.hanindya.ag5s.Model;

public class CostReference {
    String costRefName,costCreatedBy,costCreatedDateTime;

    public CostReference() {
    }

    public CostReference(String costRefName, String costCreatedBy, String costCreatedDateTime) {
        this.costRefName = costRefName;
        this.costCreatedBy = costCreatedBy;
        this.costCreatedDateTime = costCreatedDateTime;
    }

    public String getCostRefName() {
        return costRefName;
    }

    public void setCostRefName(String costRefName) {
        this.costRefName = costRefName;
    }

    public String getCostCreatedBy() {
        return costCreatedBy;
    }

    public void setCostCreatedBy(String costCreatedBy) {
        this.costCreatedBy = costCreatedBy;
    }

    public String getCostCreatedDateTime() {
        return costCreatedDateTime;
    }

    public void setCostCreatedDateTime(String costCreatedDateTime) {
        this.costCreatedDateTime = costCreatedDateTime;
    }
}
