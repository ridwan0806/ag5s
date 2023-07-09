package com.hanindya.ag5s.Model;

public class Supplies {
    private String name,category,createdBy,createdDateTime;

    public Supplies() {
    }

    public Supplies(String name, String category, String createdBy, String createdDateTime) {
        this.name = name;
        this.category = category;
        this.createdBy = createdBy;
        this.createdDateTime = createdDateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
