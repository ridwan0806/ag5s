package com.hanindya.ag5s.Model;

public class Supplies {
    private String name,category,createdBy,createdDateTime,editedBy,editedDateTime;

    public Supplies() {
    }

    public Supplies(String name, String category, String createdBy, String createdDateTime, String editedBy, String editedDateTime) {
        this.name = name;
        this.category = category;
        this.createdBy = createdBy;
        this.createdDateTime = createdDateTime;
        this.editedBy = editedBy;
        this.editedDateTime = editedDateTime;
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

    public String getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
    }

    public String getEditedDateTime() {
        return editedDateTime;
    }

    public void setEditedDateTime(String editedDateTime) {
        this.editedDateTime = editedDateTime;
    }
}
